import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Review } from '../common/review';

@Injectable({
  providedIn: 'root'
})
export class ReviewsService {
  private apiUrl = 'http://localhost:8080/reviews';

  constructor(private http: HttpClient) {}

  getReviewsByShop(shopId: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.apiUrl}/shop/${shopId}`);
  }

  getReviewsByAgent(agentId: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.apiUrl}/agent/${agentId}`);
  }

  getReviewsByStylist(stylistId: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.apiUrl}/stylist/${stylistId}`);
  }

  getAllReviews(): Observable<Review[]> {
    return this.http.get<Review[]>(this.apiUrl);
  }
  createReview(payload: any): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return this.http.post('http://localhost:8080/reviews', payload, { headers });
  }
  
}
