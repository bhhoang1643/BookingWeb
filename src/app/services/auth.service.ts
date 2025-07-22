import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { username, password });
  }

  saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  decodeToken(token: string): any {
    try {
      const payload = token.split('.')[1];
      return JSON.parse(atob(payload));
    } catch (e) {
      return null;
    }
  }

  getUserId(): number | null {
    const token = this.getToken();
    if (!token) return null;
    const decoded = this.decodeToken(token);
    return decoded?.accountId || null;
  }
  getUsername(): string | null {
    const token = this.getToken();
    if (!token) {
      console.warn('❌ No token in localStorage');
      return null;
    }
  
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const username = payload?.username || payload?.sub; 
      if (!username) {
        console.warn('❌ Username not found in token payload:', payload);
      }
      return username;
    } catch (error) {
      console.error('❌ Error decoding token:', error);
      return null;
    }
  }
  getRoles(): string[] {
    const token = this.getToken();
    if (token) {
      const decodedToken = this.decodeToken(token);
      return decodedToken?.role ? [decodedToken.role] : [];
    }
    return [];
  }

  logout(): void {
    localStorage.removeItem('token');
  }
  getAccountId(): number | null {
    const token = this.getToken();
    if (!token) return null;
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.accountId || null;
  }
  
}
