import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class JwtHelperService {
  decodeToken(token: string): any {
    if (!token) return null;
    try {
      const payload = token.split('.')[1];
      return JSON.parse(atob(payload));
    } catch (error) {
      console.error('Token decoding error:', error);
      return null;
    }
  }

  isTokenExpired(token: string): boolean {
    const decoded = this.decodeToken(token);
    if (!decoded || !decoded.exp) return true;
    const expiryTime = decoded.exp * 1000;
    return Date.now() > expiryTime;
  }

  getUserFromToken(token: string): any {
    const decoded = this.decodeToken(token);
    if (decoded) {
      return {
        username: decoded.sub || '',
        role: decoded.role || ''
      };
    }
    return null;
  }
  
}
