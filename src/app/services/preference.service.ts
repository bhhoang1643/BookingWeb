import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Preference } from '../common/preference';
import { Customer } from '../common/customer';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class PreferenceService {
  private apiUrl = 'http://localhost:8080/preferences';
  private customerUrl = 'http://localhost:8080/customers';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  getCustomerByAccountId(accountId: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.customerUrl}/by-account/${accountId}`, {
      headers: this.getHeaders()
    });
  }

  getPreferencesByCustomerId(customerId: number): Observable<Preference[]> {
    return this.http.get<Preference[]>(`${this.apiUrl}/customer/${customerId}`, {
      headers: this.getHeaders()
    });
  }

  createPreference(preference: Preference): Observable<Preference> {
    return this.http.post<Preference>(this.apiUrl, preference, {
      headers: this.getHeaders()
    });
  }

  deletePreference(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.getHeaders()
    });
  }
}
