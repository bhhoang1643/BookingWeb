import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrderDetail } from '../common/order-detail';

@Injectable({ providedIn: 'root' })
export class OrderDetailService {
  private baseUrl = 'http://localhost:8080/order-details';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('❌ No token found');
    }
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }
  
  addOrderDetail(orderId: number, productId: number, quantity: number): Observable<OrderDetail> {
    return this.http.post<OrderDetail>(
      `${this.baseUrl}/${orderId}/product/${productId}/${quantity}`,
      {},
      { headers: this.getHeaders() }
    );
  }

  getOrderDetailsByOrderId(orderId: number): Observable<OrderDetail[]> {
    return this.http.get<OrderDetail[]>(`${this.baseUrl}/order/${orderId}`, {
      headers: this.getHeaders()
    });
  }
}
