import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ServiceModel } from '../common/service';

@Injectable({
  providedIn: 'root'
})
export class ServiceService {
  private baseUrl = 'http://localhost:8080/services';

  constructor(private http: HttpClient) {}

  private getAuthHeader(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getMyServices(): Observable<ServiceModel[]> {
    return this.http.get<ServiceModel[]>(`${this.baseUrl}/my`, {
      headers: this.getAuthHeader()
    });
  }

  getServiceById(id: number): Observable<ServiceModel> {
    return this.http.get<ServiceModel>(`${this.baseUrl}/${id}`, {
      headers: this.getAuthHeader()
    });
  }

  createService(serviceData: FormData): Observable<ServiceModel> {
    return this.http.post<ServiceModel>(this.baseUrl, serviceData, {
      headers: this.getAuthHeader()
    });
  }

  updateService(id: number, serviceData: FormData): Observable<ServiceModel> {
    return this.http.put<ServiceModel>(`${this.baseUrl}/${id}`, serviceData, {
      headers: this.getAuthHeader()
    });
  }

  deleteService(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, {
      headers: this.getAuthHeader()
    });
  }

  getServicesByAgentId(agentId: number): Observable<ServiceModel[]> {
    return this.http.get<ServiceModel[]>(`${this.baseUrl}/agent/${agentId}`);
  }
}
