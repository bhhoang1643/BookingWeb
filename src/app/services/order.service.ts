import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../common/order';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private baseUrl = 'http://localhost:8080/orders';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  createOrder(order: any): Observable<Order> {
    return this.http.post<Order>(`${this.baseUrl}`, order, {
      headers: this.getHeaders()
    });
  }
  confirmOrderPayment(orderId: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/order-payments/${orderId}/confirm`, {}, {
      headers: this.getHeaders()
    });
  }  
  getOrdersByCustomer(customerId: number): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.baseUrl}/customer/${customerId}`, {
      headers: this.getHeaders()
    });
  }
}
