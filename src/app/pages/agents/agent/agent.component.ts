import { Component, OnInit } from '@angular/core';
import { NgIf, NgFor, CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Agent } from '../../../common/agent';
import { AgentService } from '../../../services/agent.service';
import { AuthService } from '../../../services/auth.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-agent',
  standalone: true,
  imports: [FormsModule, CommonModule, NgIf, NgFor],
  templateUrl: './agent.component.html',
  styleUrls: ['./agent.component.css']
})
export class AgentComponent implements OnInit {
  agent: Agent | null = null;
  newAgent: Agent = new Agent(0, '', '', '', '', '', '', '', 0);
  packages: any[] = [];

  constructor(
    private agentService: AgentService,
    private authService: AuthService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.loadAgent();
    this.loadPackages();
  }

  loadAgent(): void {
    const accountId = this.authService.getUserId();
    this.agentService.getAllAgents().subscribe(data => {
      const found = data.find(a => a.accountId === accountId);
      if (found) {
        this.agent = found;
        this.newAgent = { ...found }; 
      }
    });
  }

  loadPackages(): void {
    const accountId = this.authService.getUserId();
    this.http.get<any[]>('http://localhost:8080/api/agent-package/list').subscribe(allPackages => {
      this.packages = allPackages.filter(pkg => pkg.accountId === accountId && pkg.paymentStatus === 'paid');
    });
  }

  saveAgent(): void {
    const accountId = this.authService.getUserId();
    if (this.agent) {
      this.agentService.updateAgent(this.agent.id, this.newAgent).subscribe(() => {
        this.loadAgent();
        alert('✅ Agent updated successfully!');
      });
    } else {
      this.newAgent.accountId = accountId ?? 0;
      this.agentService.createAgent(this.newAgent).subscribe(() => {
        this.loadAgent();
        alert('✅ Agent created successfully!');
      });
    }
  }

  clearForm(): void {
    if (this.agent) {
      this.newAgent = { ...this.agent };
    } else {
      this.newAgent = new Agent(0, '', '', '', '', '', '', '', 0);
    }
  }
}
