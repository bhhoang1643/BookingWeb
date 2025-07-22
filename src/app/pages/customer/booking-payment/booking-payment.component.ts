import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { CustomerService } from '../../../services/customer.service';
import { AuthService } from '../../../services/auth.service';
import { BookingService } from '../../../services/booking.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-payment',
  standalone: true,
  templateUrl: './booking-payment.component.html',
  styleUrls: ['./booking-payment.component.css'],
  imports: [CommonModule, FormsModule]
})
export class BookingPaymentComponent implements OnInit {
  paymentType: 'BOOKING' | 'ORDER' = 'BOOKING';
  id: number = 0;

  amount: number = 0;
  originalAmount: number = 0;
  method: string = 'CASH';
  pointBalance: number = 0;
  usedPoints: number = 0;
  maxUsablePoints: number = 0;
  isProcessing: boolean = false;
  apiUrl: string = 'http://localhost:8080';

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    private customerService: CustomerService,
    private authService: AuthService,
    private bookingService: BookingService
  ) {}

  ngOnInit(): void {
    const type = this.route.snapshot.queryParamMap.get('type');
    this.paymentType = type === 'order' ? 'ORDER' : 'BOOKING';

    this.id = Number(this.route.snapshot.paramMap.get(this.paymentType === 'ORDER' ? 'orderId' : 'bookingId'));
    this.amount = Number(this.route.snapshot.queryParamMap.get('amount'));
    this.originalAmount = this.amount;

    this.loadCustomerPoint();

    if (this.paymentType === 'BOOKING') {
      this.refreshBookingAmount();
    } else {
      this.refreshOrderAmount();
    }
  }

  loadCustomerPoint(): void {
    const accountId = this.authService.getAccountId();
    if (accountId) {
      this.customerService.getCustomerByAccountId(accountId).subscribe({
        next: data => {
          this.pointBalance = data.point ?? 0;
          this.updateMaxUsablePoints();
        },
        error: err => console.error('❌ Error in getting scoreError in getting score:', err)
      });
    }
  }

  refreshBookingAmount(): void {
    this.bookingService.getBookingById(this.id).subscribe({
      next: data => {
        this.amount = data.totalPrice;
        this.updateMaxUsablePoints();
      },
      error: err => console.error('❌ Error updating booking price:', err)
    });
  }

  refreshOrderAmount(): void {
    this.http.get<any>(`${this.apiUrl}/orders/${this.id}`).subscribe({
      next: data => {
        this.amount = data.totalPrice;
        this.updateMaxUsablePoints();
      },
      error: err => console.error('❌ Error updating order price:', err)
    });
  }

  get remainingAmount(): number {
    const discount = this.usedPoints * 1000;
    return Math.max(this.amount - discount, 0);
  }
  validateUsedPoints(): void {
    if (this.usedPoints > this.maxUsablePoints) {
      this.usedPoints = this.maxUsablePoints;
    }
  }
  

  updateMaxUsablePoints(): void {
    this.maxUsablePoints = Math.min(this.pointBalance, Math.floor(this.amount / 1000));
    if (this.usedPoints > this.maxUsablePoints) {
      this.usedPoints = this.maxUsablePoints;
    }
  }

  onUsedPointsChange(): void {
    this.updateMaxUsablePoints();
  }

  pay(): void {
    if (this.usedPoints > this.maxUsablePoints || this.usedPoints < 0) {
      alert("⚠️ Invalid point value.");
      return;
    }
  
    this.isProcessing = true;
  
    const token = localStorage.getItem('token') || '';
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const finalAmount = this.remainingAmount;
  
    const refPrefix = this.paymentType === 'BOOKING' ? 'BOOK' : 'ORDER';
    const txnRef = `${refPrefix}_${this.id}_PTS_${this.usedPoints}`;
    const returnUrl = encodeURIComponent('http://localhost:4200/vnpay-return');
  
    if (this.method === 'VNPAY') {
      const fakeBankUrl = `http://localhost:4200/fake-bank?vnp_TxnRef=${txnRef}&vnp_Amount=${finalAmount}&vnp_ReturnUrl=${returnUrl}`;
      window.location.href = fakeBankUrl;
      this.isProcessing = false;
      return;
    }
  
    const endpoint = this.paymentType === 'BOOKING'
      ? `/payments/booking/${this.id}`
      : `/payments/order/${this.id}`;
  
    const params = new HttpParams()
      .set('method', this.method)
      .set('usedPoints', this.usedPoints.toString());
  
    this.http.post<any>(`${this.apiUrl}${endpoint}`, null, { headers, params }).subscribe({
      next: (res) => {
        this.paymentType === 'BOOKING'
          ? this.refreshBookingAmount()
          : this.refreshOrderAmount();
  
        alert(`✅ Payment Successful!\nPoints used: ${res.pointUsed}\nRemaining amount: ${res.remainingAmount.toLocaleString()} VND`);
        this.router.navigate(['/customer/main']);
      },
      error: err => {
        console.error('❌ Payment error:', err);
        alert('❌ Payment failed!');
      },
      complete: () => this.isProcessing = false
    });
  }
}
