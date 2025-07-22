import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Agent } from '../common/agent';

@Injectable({
  providedIn: 'root'
})
export class AgentService {
  private baseUrl = 'http://localhost:8080/agents';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getAllAgents(): Observable<Agent[]> {
    return this.http.get<Agent[]>(this.baseUrl, { headers: this.getHeaders() });
  }
  getMyAgent(): Observable<Agent> {
    return this.http.get<Agent>(`${this.baseUrl}/me`, { headers: this.getHeaders() });
  }
  
  getAgentById(id: number): Observable<Agent> {
    return this.http.get<Agent>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }

  createAgent(agent: Agent): Observable<Agent> {
    return this.http.post<Agent>(this.baseUrl, agent, { headers: this.getHeaders() });
  }

  updateAgent(id: number, agent: Agent): Observable<Agent> {
    return this.http.put<Agent>(`${this.baseUrl}/${id}`, agent, { headers: this.getHeaders() });
  }

  deleteAgent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }

  
}