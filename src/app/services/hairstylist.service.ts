import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Hairstylist } from '../common/hairstylist';
import { map } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class HairstylistService {
  private baseUrl = 'http://localhost:8080/hairstylists';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  getAll(): Observable<Hairstylist[]> {
    return this.http.get<Hairstylist[]>(this.baseUrl, { headers: this.getHeaders() });
  }

  createHairstylist(formData: FormData): Observable<Hairstylist> {
    return this.http.post<Hairstylist>(this.baseUrl, formData, { headers: this.getHeaders() });
  }

  updateHairstylist(id: number, formData: FormData): Observable<Hairstylist> {
    return this.http.put<Hairstylist>(`${this.baseUrl}/${id}`, formData, { headers: this.getHeaders() });
  }

  deleteHairstylist(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }

  getByShopId(shopId: number): Observable<Hairstylist[]> {
    return this.http.get<Hairstylist[]>(`${this.baseUrl}/shop/${shopId}`, {
      headers: this.getHeaders()
    });
  }

  getHairstylistsByMyShop(shopId: number): Observable<Hairstylist[]> {
  return this.http.get<any[]>(`${this.baseUrl}/my/shop/${shopId}`, {
    headers: this.getHeaders()
  }).pipe(
    map((list: any[]) => list.map(item => new Hairstylist(
      item.stylistId ?? item.id, 
      item.shopId,
      item.name,
      item.experience,
      item.specialty,
      item.image
    )))
  );
}

}
