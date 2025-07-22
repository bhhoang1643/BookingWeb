import { Component, OnInit } from '@angular/core';
import { ServiceService } from '../../../services/service.service';
import { ServiceModel } from '../../../common/service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-customer-service',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customer-service.component.html',
  styleUrls: ['./customer-service.component.css']
})
export class CustomerServiceComponent implements OnInit {
  agents: any[] = [];
  services: ServiceModel[] = [];
  filteredServices: ServiceModel[] = [];
  searchText: string = '';
  filteredAgents: any[] = [];
  constructor(private serviceService: ServiceService, private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.loadAgentsWithServices();
  }
   scrollLeft(agentId: string | number) {
    const container = document.getElementById(`scroll-${agentId}`);
    if (container) container.scrollLeft -= 300;
  }

  scrollRight(agentId: string | number) {
    const container = document.getElementById(`scroll-${agentId}`);
    if (container) container.scrollLeft += 300;
  }
  loadAgentsWithServices(): void {
    this.http.get<any[]>('http://localhost:8080/services/agents-with-services/all').subscribe((data) => {
      this.agents = data;
      this.filteredAgents = data; 
    });
  }
  
  filterServicesByAgent(): void {
    const keyword = this.searchText.trim().toLowerCase();
    this.filteredAgents = keyword
      ? this.agents.filter(agent => agent.agentName.toLowerCase().includes(keyword))
      : this.agents;
  }

  bookNow(agentId: number, serviceId: number): void {
    this.router.navigate(['/customer/booking'], {
      queryParams: {
        agentId: agentId,
        preselectServiceId: serviceId
      }
    });
  }  
}
