import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AgentPackage } from '../common/agent-package'; // nhớ đường dẫn đúng

@Injectable({
  providedIn: 'root'
})
export class PackageService {

  private baseUrl = 'http://localhost:8080/api/agent-package';

  constructor(private http: HttpClient) { }

  private createAuthorizationHeader(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  // ✅ Lấy tất cả package
  getAllPackages(): Observable<AgentPackage[]> {
    const headers = this.createAuthorizationHeader();
    return this.http.get<AgentPackage[]>(`${this.baseUrl}/list`, { headers });
  }

  // ✅ Tạo package mới
  createPackage(data: { accountId: number; name: string; price: number; duration: number }): Observable<AgentPackage> {
    const headers = this.createAuthorizationHeader();
    return this.http.post<AgentPackage>(`${this.baseUrl}/subscribe`, data, { headers });
  }

  // ✅ Cập nhật package
  updatePackage(id: number, data: { name: string; price: number; duration: number }): Observable<AgentPackage> {
    const headers = this.createAuthorizationHeader();
    return this.http.put<AgentPackage>(`${this.baseUrl}/update/${id}`, data, { headers });
  }

  // ✅ Xoá package
  deletePackage(id: number): Observable<any> {
    const headers = this.createAuthorizationHeader();
    return this.http.delete(`${this.baseUrl}/delete/${id}`, { headers, responseType: 'text' });
  }

  // ✅ Xác nhận thanh toán cho package
  confirmPayment(subscriptionId: number): Observable<any> {
    const headers = this.createAuthorizationHeader();
    return this.http.post(`${this.baseUrl}/confirm-payment?subscriptionId=${subscriptionId}`, {}, { headers, responseType: 'text' });
  }
}
