import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../../../services/auth.service';
import { CommonModule, DatePipe, DecimalPipe, NgClass } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-order-management',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, ReactiveFormsModule, NgClass],
  providers: [DatePipe, DecimalPipe],
  templateUrl: './order-management.component.html',
  styleUrls: ['./order-management.component.css']
})
export class OrderManagementComponent implements OnInit {
  orders: any[] = [];
  filteredOrders: any[] = [];
  apiUrl: string = 'http://localhost:8080';

  searchText: string = '';
  selectedDate: string = '';
  selectedCategory: string = '';
  loading: boolean = false;

  currentPage: number = 1;
  itemsPerPage: number = 5;

  constructor(private http: HttpClient, private authService: AuthService) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true;
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${this.authService.getToken()}` });
    this.http.get<any[]>(`${this.apiUrl}/orders/my`, { headers }).subscribe({
      next: data => {
        this.orders = data.sort((a, b) => b.id - a.id); 
        this.loadOrderDetails();
      },
      error: err => {
        console.error('❌ Fail to load Orders:', err);
        this.loading = false;
      }
    });
  }

  loadOrderDetails(): void {
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${this.authService.getToken()}` });
    let count = 0;

    this.orders.forEach(order => {
      this.http.get<any[]>(`${this.apiUrl}/order-details/order/${order.id}`, { headers }).subscribe({
        next: details => {
          order.details = details;
          count++;
          if (count === this.orders.length) {
            this.filteredOrders = [...this.orders];
            this.loading = false;
          }
        },
        error: err => {
          console.error('❌ fail to load OrderDetails:', err);
          count++;
          if (count === this.orders.length) {
            this.filteredOrders = [...this.orders];
            this.loading = false;
          }
        }
      });
    });
  }

  get totalRevenue(): number {
    return this.filteredOrders
      .filter(order => order.paymentStatus === 'paid')
      .reduce((total, order) => total + (order.totalPrice || 0), 0);
  }

  get uniqueCategories(): string[] {
    const categories = new Set<string>();
    this.orders.forEach(order => {
      order.details?.forEach((d: any) => categories.add(d.categoryName));
    });
    return Array.from(categories);
  }

  applyFilters(): void {
    this.filteredOrders = this.orders.filter(order => {
      const matchesText = this.searchText.trim() === '' || 
        `${order.customerName} ${order.customerPhone} ${order.customerAddress} ${order.details?.map((d: any) => `${d.productName} ${d.categoryName}`).join(' ')}`.toLowerCase()
        .includes(this.searchText.toLowerCase());

      const matchesDate = this.selectedDate === '' || 
        (order.createdAt && order.createdAt.startsWith(this.selectedDate));

      const matchesCategory = this.selectedCategory === '' || 
        (order.details && order.details.some((d: any) => d.categoryName === this.selectedCategory));

      return matchesText && matchesDate && matchesCategory;
    }).sort((a, b) => b.id - a.id); 
    this.currentPage = 1;
  }

  confirmOrder(orderId: number): void {
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${this.authService.getToken()}` });
    this.http.put<string>(`${this.apiUrl}/orders/order-payments/${orderId}/confirm`, null, {
      headers,
      responseType: 'text' as 'json'
    }).subscribe({
      next: (response) => {
        alert(response); 
        this.loadOrders();
      },
      error: err => {
        console.error('❌ Fail to confirm Order:', err);
        alert('❌ Confirm fail!');
      }
    });    
  }

  get pagedOrders(): any[] {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    return this.filteredOrders.slice(start, start + this.itemsPerPage);
  }

  get totalPages(): number {
    return Math.ceil(this.filteredOrders.length / this.itemsPerPage);
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }
}
