import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Customer } from '../common/customer';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = 'http://localhost:8080/customers';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  getCustomerByAccountId(accountId: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}/by-account/${accountId}`, {
      headers: this.getHeaders()
    });
  }
  
  createCustomer(customer: Customer, file?: File): Observable<any> {
    const formData = new FormData();
    formData.append('birthYear', customer.birthYear?.toString() || '');
    formData.append('gender', customer.gender || '');
    formData.append('address', customer.address || '');
    formData.append('point', String(customer.point ?? 0));
    formData.append('accountId', String(customer.accountId ?? 0));
    if (file) {
      formData.append('imageFile', file);
    }

    const headers = this.getHeaders();
    return this.http.post(this.apiUrl, formData, { headers });
  }

  updateCustomer(id: number, customer: Customer, file?: File): Observable<any> {
    const formData = new FormData();
    formData.append('birthYear', customer.birthYear?.toString() || '');
    formData.append('gender', customer.gender || '');
    formData.append('address', customer.address || '');
    formData.append('point', String(customer.point ?? 0));
    if (file) {
      formData.append('imageFile', file);
    }
    const headers = this.getHeaders();
    return this.http.put(`${this.apiUrl}/${id}`, formData, { headers });
  }
}
