import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule, formatDate } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormControl } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgClass, DatePipe, DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-booking-agent',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, ReactiveFormsModule, NgClass],
  providers: [DatePipe, DecimalPipe],
  templateUrl: './agent-booking.component.html',
  styleUrls: ['./agent-booking.component.css']
})
export class BookingAgentComponent implements OnInit {
  bookings: any[] = [];
  filteredBookings: any[] = [];
  pagedBookings: any[] = [];
  apiUrl: string = 'http://localhost:8080';
  token: string = localStorage.getItem('token') || '';
  stylistFilter = new FormControl('');
  dateFilter = new FormControl('');
  shopFilter = new FormControl('');
  shopOptions: string[] = [];
  totalAmount: number = 0;
  currentPage: number = 1;
  pageSize: number = 5;
  totalPages: number = 0;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchBookings();
  }

  fetchBookings(): void {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}`);
    this.http.get<any[]>(`${this.apiUrl}/bookings/my`, { headers }).subscribe({
      next: (data) => {
        this.bookings = data.sort((a, b) => new Date(b.datetime).getTime() - new Date(a.datetime).getTime());
        this.filteredBookings = [...this.bookings];
        this.shopOptions = [...new Set(data.map(b => b.shopLocation))];
        this.updatePagination();
        this.calculateTotal();
      },
      error: (err) => console.error('❌ Fail while load bookings:', err)
    });
  }

  confirmPayment(bookingId: number): void {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}`);
    this.http.put(`${this.apiUrl}/bookings/booking-payments/${bookingId}/confirm`, {}, { headers }).subscribe({
      next: () => {
        alert('✅ Confirm success!');
        this.fetchBookings();
      },
      error: (err) => alert('❌ Fail while confirm: ' + err.message)
    });
  }

  applyFilters(): void {
    const stylist = this.stylistFilter.value?.toLowerCase() || '';
    const date = this.dateFilter.value;
    const selectedShop = this.shopFilter.value;

    this.filteredBookings = this.bookings.filter(b => {
      const matchDate = date ? formatDate(b.datetime, 'yyyy-MM-dd', 'en-US') === date : true;
      const matchStylist = stylist ? b.stylistName?.toLowerCase().includes(stylist) : true;
      const matchShop = selectedShop ? b.shopLocation === selectedShop : true;
      return matchDate && matchStylist && matchShop;
    });

    this.updatePagination();
    this.calculateTotal();
  }

  updatePagination(): void {
    this.totalPages = Math.ceil(this.filteredBookings.length / this.pageSize);
    this.setPagedBookings();
  }

  setPagedBookings(): void {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.pagedBookings = this.filteredBookings.slice(startIndex, endIndex);
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.setPagedBookings();
    }
  }

  calculateTotal(): void {
    this.totalAmount = this.filteredBookings
      .filter(b => b.paymentStatus?.toLowerCase() === 'paid')
      .reduce((sum, booking) => sum + (booking.totalPrice || 0), 0);
  }

  clearFilters(): void {
    this.stylistFilter.setValue('');
    this.dateFilter.setValue('');
    this.shopFilter.setValue('');
    this.filteredBookings = [...this.bookings];
    this.updatePagination();
    this.calculateTotal();
  }

  getServiceNames(services: any[]): string {
    return services?.map(s => s.name).join(', ') || '';
  }
}