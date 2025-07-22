import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Booking } from '../common/booking';
import { BookingDetail } from '../common/booking-detail';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private bookingUrl = 'http://localhost:8080/bookings';
  private bookingDetailUrl = 'http://localhost:8080/booking-details';

  constructor(private http: HttpClient) {}

  // -------------------------
  // 📅 Booking APIs
  // -------------------------

  getAllBookings(): Observable<Booking[]> {
    return this.http.get<Booking[]>(this.bookingUrl);
  }

  getBookingById(id: number): Observable<Booking> {
    return this.http.get<Booking>(`${this.bookingUrl}/${id}`);
  }

  getBookingsByCustomerId(customerId: number): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.bookingUrl}/customer/${customerId}`);
  }

  getBookingsByShopId(shopId: number): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.bookingUrl}/shop/${shopId}`);
  }

  createBooking(booking: Booking): Observable<Booking> {
    return this.http.post<Booking>(this.bookingUrl, booking);
  }

  updateBooking(id: number, booking: Booking): Observable<Booking> {
    return this.http.put<Booking>(`${this.bookingUrl}/${id}`, booking);
  }

  deleteBooking(id: number): Observable<any> {
    return this.http.delete(`${this.bookingUrl}/${id}`, { responseType: 'text' });
  }

  // -------------------------
  // 🧾 Booking Detail APIs
  // -------------------------

  getAllBookingDetails(): Observable<BookingDetail[]> {
    return this.http.get<BookingDetail[]>(this.bookingDetailUrl);
  }

  getBookingDetailsByBookingId(bookingId: number): Observable<BookingDetail[]> {
    return this.http.get<BookingDetail[]>(`${this.bookingDetailUrl}/booking/${bookingId}`);
  }

  
  createBookingDetail(detail: BookingDetail): Observable<BookingDetail[]> {
    return this.http.post<BookingDetail[]>(this.bookingDetailUrl, detail);
  }

  updateBookingDetail(id: number, detail: BookingDetail): Observable<BookingDetail> {
    return this.http.put<BookingDetail>(`${this.bookingDetailUrl}/${id}`, detail);
  }

  deleteBookingDetail(id: number): Observable<any> {
    return this.http.delete(`${this.bookingDetailUrl}/${id}`, { responseType: 'text' });
  }
}
