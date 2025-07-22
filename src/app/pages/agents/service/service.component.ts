import { Component, OnInit } from '@angular/core';
import { CommonModule, NgIf, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ServiceModel } from '../../../common/service';
import { ServiceService } from '../../../services/service.service';
import { AgentService } from '../../../services/agent.service';
import { Agent } from '../../../common/agent';

@Component({
  selector: 'app-service',
  standalone: true,
  imports: [CommonModule, FormsModule, NgIf, NgFor],
  templateUrl: './service.component.html',
  styleUrls: ['./service.component.css']
})
export class ServiceComponent implements OnInit {
  services: ServiceModel[] = [];
  newService: ServiceModel = new ServiceModel(0, 0, '', 0, '', '');
  selectedService: ServiceModel | null = null;
  selectedFile: File | null = null;
  showModal: boolean = false;
  constructor(
    private serviceService: ServiceService,
    private agentService: AgentService
  ) {}

  ngOnInit(): void {
    this.loadMyServices();
  }

  loadMyServices(): void {
    this.serviceService.getMyServices().subscribe(data => this.services = data);
  }

  onFileSelected(event: Event): void {
    const fileInput = event.target as HTMLInputElement;
    if (fileInput.files && fileInput.files.length > 0) {
      this.selectedFile = fileInput.files[0];
    }
  }

  save(): void {
    
    if (!this.newService.name || this.newService.price <= 0 || !this.newService.status) {
      alert('❌ Please fill in all required fields!');
      return;
    }
  
    this.agentService.getMyAgent().subscribe({
      next: (agent: Agent) => {
        const formData = new FormData();
        formData.append('name', this.newService.name);
        formData.append('price', this.newService.price.toString());
        formData.append('status', this.newService.status);
        if (this.selectedFile) {
          formData.append('file', this.selectedFile);
        } else if (!this.selectedService) {
          alert('❌ Please upload an image for the service!');
          return;
        }
  
        if (this.selectedService) {
          this.serviceService.updateService(this.selectedService.serviceId, formData)
            .subscribe(() => {
              this.loadMyServices();
              this.clearForm();
            });
        } else {
          this.serviceService.createService(formData)
            .subscribe(() => {
              this.loadMyServices();
              this.clearForm();
            });
        }
      },
      error: () => alert('❌ Cannot find Agent information!')
    });
  }
  

  edit(service: ServiceModel): void {
    this.selectedService = service;
    this.newService = { ...service };
    this.selectedFile = null;
  }

  delete(id: number): void {
    this.serviceService.deleteService(id).subscribe(() => this.loadMyServices());
  }

  clearForm(): void {
    this.newService = new ServiceModel(0, 0, '', 0, '', '');
    this.selectedService = null;
    this.selectedFile = null;
  }
  openModal(): void {
    this.showModal = true;
  }
  
  closeModal(): void {
    this.showModal = false;
    this.clearForm();
  }
}