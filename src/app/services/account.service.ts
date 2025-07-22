import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Account } from '../common/account';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private apiUrl = 'http://localhost:8080/auth';  

  constructor(private http: HttpClient) {}

 
  getAccountList(): Observable<Account[]> {
    const headers = this.createAuthorizationHeader();
    return this.http.get<Account[]>(`${this.apiUrl}/accounts`, { headers });
  }

  
  register(username: string, password: string, email: string, phoneNumber: string, role: string): Observable<any> {
    const body = { username, password, email, phoneNumber, role };
    return this.http.post<any>(`${this.apiUrl}/register`, body);
  }

  
  disableAccount(id: number): Observable<any> {
    const headers = this.createAuthorizationHeader();
    return this.http.patch(`${this.apiUrl}/disable/${id}`, {}, { headers });
  }

 
  deleteAccount(id: number): Observable<any> {
    const headers = this.createAuthorizationHeader();
    return this.http.delete(`${this.apiUrl}/delete/${id}`, { headers });
  }

  private createAuthorizationHeader(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }
}
