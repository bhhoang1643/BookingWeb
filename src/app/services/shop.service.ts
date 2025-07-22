import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Shop } from '../common/shop';

@Injectable({
  providedIn: 'root'
})
export class ShopService {
  private baseUrl = 'http://localhost:8080/shops';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getAllShops(): Observable<Shop[]> {
    return this.http.get<Shop[]>(this.baseUrl, { headers: this.getHeaders() });
  }

  getShopsByAgentId(agentId: number): Observable<Shop[]> {
    return this.http.get<Shop[]>(`${this.baseUrl}/agent/${agentId}`, { headers: this.getHeaders() });
  }

  getShopById(id: number): Observable<Shop> {
    return this.http.get<Shop>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }

  createShop(shop: Shop): Observable<Shop> {
    return this.http.post<Shop>(this.baseUrl, shop, { headers: this.getHeaders() });
  }

  updateShop(id: number, shop: Shop): Observable<Shop> {
    return this.http.put<Shop>(`${this.baseUrl}/${id}`, shop, { headers: this.getHeaders() });
  }

  deleteShop(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }
  getMyShops(): Observable<Shop[]> {
    return this.http.get<Shop[]>(`${this.baseUrl}/my`, { headers: this.getHeaders() });
  }
  
}