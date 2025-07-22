import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BookingService } from '../../../services/booking.service';
import { CustomerService } from '../../../services/customer.service';
import { AuthService } from '../../../services/auth.service';
import { AgentService } from '../../../services/agent.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Booking } from '../../../common/booking';
import { BookingDetail } from '../../../common/booking-detail';

@Component({
  selector: 'app-booking',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.css']
})
export class BookingComponent implements OnInit {
  agentId: number = 0;
  shopId: number = 0;
  selectedStylistId: number | null = null;
  selectedServiceIds: number[] = [];
  selectedDateTime: string = '';
  selectedDate: string = '';
  selectedTime: string = '';
  isDateConflict: boolean = false;

  timeSlots: string[] = [
    "08:00", "09:00", "10:00", "11:00",
    "12:00", "13:00", "14:00", "15:00",
    "16:00", "17:00", "18:00", "19:00",
    "20:00", "21:00", "22:00"
  ];
  unavailableSlots: string[] = [];

  customerId: number = 0;
  shops: any[] = [];
  stylists: any[] = [];
  services: any[] = [];
  agents: any[] = [];

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private bookingService: BookingService,
    private customerService: CustomerService,
    private authService: AuthService,
    private agentService: AgentService
  ) { }

  ngOnInit(): void {
    const accountId = this.authService.getAccountId();
    if (accountId) {
      this.customerService.getCustomerByAccountId(accountId).subscribe(data => {
        this.customerId = data.id ?? 0;
      });
    }

    this.agentService.getAllAgents().subscribe(data => this.agents = data);

    this.route.queryParams.subscribe(params => {
      this.agentId = +params['agentId'];
      const preselectServiceId = +params['preselectServiceId'];

      this.loadShopsByAgentId(this.agentId);
      this.loadServicesByAgentId(this.agentId);

      if (preselectServiceId) {
        this.selectedServiceIds = [preselectServiceId];
      }
    });
  }

  getAgentNameById(agentId: number): string {
    const agent = this.agents.find(a => a.id === agentId);
    return agent ? agent.agentName : 'Unknown';
  }

  loadShopsByAgentId(agentId: number) {
    const headers = new HttpHeaders({ Authorization: `Bearer ${this.authService.getToken()}` });
    this.http.get<any[]>(`http://localhost:8080/shops/agent/${agentId}`, { headers })
      .subscribe(data => this.shops = data);
  }

  loadServicesByAgentId(agentId: number) {
    this.http.get<any[]>(`http://localhost:8080/services/agent/${agentId}`)
      .subscribe(data => this.services = data);
  }

  onShopSelected() {
    if (this.shopId) {
      this.http.get<any[]>(`http://localhost:8080/hairstylists/shop/${this.shopId}`)
        .subscribe(data => this.stylists = data);
    } else {
      this.stylists = [];
    }
  }

  toggleService(id: number) {
    const index = this.selectedServiceIds.indexOf(id);
    if (index > -1) this.selectedServiceIds.splice(index, 1);
    else this.selectedServiceIds.push(id);
  }

  calculateTotalPrice(): number {
    return this.selectedServiceIds.reduce((sum, id) => {
      const service = this.services.find(s => s.serviceId === id);
      return sum + (service ? service.price : 0);
    }, 0);
  }

  toIsoDatetime(dateStr: string, timeStr: string): string {
    const [year, month, day] = dateStr.split('-').map(Number);
    const [hour, minute] = timeStr.split(':').map(Number);
    const date = new Date(year, month - 1, day, hour, minute);

    if (isNaN(date.getTime())) {
      throw new Error(`Invalid date: ${dateStr}T${timeStr}`);
    }


    return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}T${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}:00`;
  }


  updateDateTime() {
    if (!this.selectedDate || !this.selectedTime) {
      console.warn("⚠️ No valid date or time selected");
      return;
    }

    if (this.isPastDate(this.selectedDate)) {
      alert("⛔ You cannot book a date in the past.");
      this.selectedDate = '';
      return;
    }

    const timeRegex = /^\d{2}:\d{2}$/;
    if (!timeRegex.test(this.selectedTime)) {
      console.error("⛔ Time format error:", this.selectedTime);
      return;
    }

    if (this.isPastSlot(this.selectedDate, this.selectedTime)) {
      alert("⛔ You cannot book a time in the past.");
      this.selectedTime = '';
      return;
    }

    try {
      this.selectedDateTime = this.toIsoDatetime(this.selectedDate, this.selectedTime);
      console.log("📅 selectedDateTime:", this.selectedDateTime);

      if (this.unavailableSlots.includes(this.selectedTime)) {
        this.isDateConflict = true;
        return;
      }

      this.checkConflictTime();
    } catch (e) {
      console.error("⛔ Date creation error:", e);
    }
  }


  selectTime(hour: string) {
    if (
      this.unavailableSlots.includes(hour) ||
      this.isPastSlot(this.selectedDate, hour)
    ) return;

    this.selectedTime = hour;
    this.updateDateTime();
  }


  loadUnavailableSlots() {
    if (!this.selectedDate || !this.selectedStylistId) {
      this.unavailableSlots = [];
      return;
    }

    const headers = new HttpHeaders({ Authorization: `Bearer ${this.authService.getToken()}` });

    this.http.get<any[]>(`http://localhost:8080/booking-details/stylist/${this.selectedStylistId}`, { headers })
      .subscribe(details => {
        this.unavailableSlots = details
          .map(detail => {
            const dtStr = detail.datetime || detail.booking?.datetime;
            return new Date(dtStr);
          })
          .filter(dt => dt.toISOString().startsWith(this.selectedDate))
          .map(dt => dt.toTimeString().slice(0, 5));
      });
  }

  checkConflictTime() {
    if (!this.selectedDateTime || !this.selectedStylistId) {
      this.isDateConflict = false;
      return;
    }

    const headers = new HttpHeaders({ Authorization: `Bearer ${this.authService.getToken()}` });
    this.http.get<boolean>(
      `http://localhost:8080/booking-details/check-availability?stylistId=${this.selectedStylistId}&datetime=${this.selectedDateTime}`,
      { headers }
    ).subscribe(
      (isAvailable) => this.isDateConflict = !isAvailable,
      (error) => {
        console.error('Stylist check error:', error);
        this.isDateConflict = false;
      }
    );
  }

  submitBooking() {
    if (!this.customerId || !this.shopId || !this.selectedDateTime || !this.selectedStylistId || this.selectedServiceIds.length === 0) {
      alert("Please enter complete information.");
      return;
    }

    this.loadUnavailableSlots();

    setTimeout(() => {
      if (this.unavailableSlots.includes(this.selectedTime)) {
        alert("⛔ The time slot is already booked. Please select another time slot..");
        return;
      }

      if (this.isDateConflict) {
        alert("Stylist is booked for this time, please choose another time.");
        return;
      }

      const totalPrice = this.calculateTotalPrice();
      const booking: Booking = {
        customerId: this.customerId,
        shopId: this.shopId,
        agentId: this.agentId,
        datetime: this.selectedDateTime,
        paymentStatus: 'UNPAID',
        totalPrice
      };
      this.bookingService.createBooking(booking).subscribe(created => {
        const detail: BookingDetail = {
          bookingId: created.bookingId!,
          stylistId: this.selectedStylistId!,
          serviceIds: this.selectedServiceIds
        };
        this.bookingService.createBookingDetail(detail).subscribe(() => {

          this.router.navigate(['/customer/payment/booking', created.bookingId], {
            queryParams: { amount: totalPrice }
          });
        });
      });
    }, 200);
  }
  isPastSlot(dateStr: string, timeStr: string): boolean {
    const now = new Date();
    const [year, month, day] = dateStr.split('-').map(Number);
    const [hour, minute] = timeStr.split(':').map(Number);
    const date = new Date(year, month - 1, day, hour, minute);
    return date.getTime() < now.getTime();
  }
  isPastDate(dateStr: string): boolean {
    const today = new Date();
    const [year, month, day] = dateStr.split('-').map(Number);
    const selected = new Date(year, month - 1, day);
    return selected < new Date(today.getFullYear(), today.getMonth(), today.getDate());
  }
}
