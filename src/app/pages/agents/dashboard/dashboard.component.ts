import { CommonModule } from '@angular/common';
import { Component, HostListener, OnInit } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterModule, CommonModule, RouterOutlet, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  username: string = '';
  sidebarCollapsed = false;
  dropdownOpen: { [key: string]: boolean } = {};
  userDropdownOpen = false;

  remainingMonths = 0;
  remainingDays = 0;
  remainingHours = 0;
  remainingMinutes = 0;

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit(): void {
    this.loadUserInfo();
  }

  toggleSidebar(): void {
    this.sidebarCollapsed = !this.sidebarCollapsed;
  }

  toggleDropdown(menu: string): void {
    this.dropdownOpen[menu] = !this.dropdownOpen[menu];
  }

  toggleUserDropdown(event: MouseEvent): void {
    event.stopPropagation();
    this.userDropdownOpen = !this.userDropdownOpen;
  }

  @HostListener('document:click', ['$event'])
  closeDropdownOutside(event: MouseEvent): void {
    if (this.userDropdownOpen) {
      this.userDropdownOpen = false;
    }
  }

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  loadUserInfo(): void {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        this.username = payload.username || 'User';
        const accountId = payload.accountId;

        this.fetchRemainingTime(accountId, token);
      } catch (error) {
        console.error('Invalid token', error);
      }
    }
  }

  fetchRemainingTime(accountId: number, token: string): void {
    this.http.get<any>(`http://localhost:8080/api/agent-package/remaining/${accountId}`, {
      headers: { Authorization: `Bearer ${token}` }
    }).subscribe({
      next: (data) => {
        this.remainingMonths = data.months || 0;
        this.remainingDays = data.days || 0;
        this.remainingHours = data.hours || 0;
        this.remainingMinutes = data.minutes || 0;
      },
      error: (err) => {
        console.error('Error fetching package remaining time:', err);
      }
    });
  }
}
