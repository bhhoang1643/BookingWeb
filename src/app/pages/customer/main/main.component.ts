import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ReviewsService } from '../../../services/reviews.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-customer-main',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
  @ViewChild('scrollContainer') scrollContainer!: ElementRef;

  searchQuery: string = '';
  filteredAgents: any[] = [];
  selectedAgent: any = null;
  showWarning: boolean = false;
  stars = [1, 2, 3, 4, 5];
  allAgents: any[] = [];
  allStylists: any[] = [];
  allShops: any[] = [];
  shops: any[] = [];
  stylists: any[] = [];
  customerId: number = 0;
  totalPages: number = 0;
  topRatedAgents: { agentName: string, avgRating: number, totalStars: number, count: number }[] = [];
  topStylists: any[] = [];
  reviews: any[] = [];
  paginatedReviews: any[] = [];
  currentPage: number = 1;
  pageSize: number = 5;
  canScrollRight = false;
  currentSlide = 0;

  newReview = {
    rating: 0,
    comment: '',
    agentId: null,
    shopId: null,
    hairstylistId: null
  };

  constructor(
    private http: HttpClient,
    private router: Router,
    private reviewService: ReviewsService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.loadAgents();
    this.loadReviews();
    this.loadCustomerIdFromAccount();
    this.loadTopStylists();
  }

  ngAfterViewInit() {
    this.updateScrollButtons();
  }

  scrollLeft() {
    const el = this.scrollContainer.nativeElement as HTMLElement;
    el.scrollLeft -= 220;
  }
  scrollRight() {
    const el = this.scrollContainer.nativeElement as HTMLElement;
    el.scrollLeft += 220;
  }
  

  updateScrollButtons() {
    const el = this.scrollContainer.nativeElement;
    this.canScrollRight = el.scrollLeft + el.clientWidth < el.scrollWidth;
    this.currentSlide = Math.floor(el.scrollLeft / 220);
  }

  loadAgents(): void {
    this.http.get<any[]>('http://localhost:8080/agents').subscribe(data => {
      this.allAgents = data;
    });
  }

  loadReviews(): void {
    this.reviewService.getAllReviews().subscribe(data => {
      this.reviews = data.sort((a, b) => {
        const dateA = a.timestamp ? new Date(a.timestamp).getTime() : 0;
        const dateB = b.timestamp ? new Date(b.timestamp).getTime() : 0;
        return dateB - dateA;
      });
      this.paginateReviews();
      this.calculateTopAgents();
    });
  }

  loadCustomerIdFromAccount(): void {
    const accountId = this.authService.getAccountId();
    this.http.get<any>(`http://localhost:8080/customers/by-account/${accountId}`).subscribe({
      next: (data) => {
        this.customerId = data.id;
        console.log('✅ Customer ID loaded:', this.customerId);
      },
      error: (err) => {
        console.error('Cannot find customer from accountId:', err);
      }
    });
  }

  paginateReviews(): void {
    this.totalPages = Math.ceil(this.reviews.length / this.pageSize);
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.paginatedReviews = this.reviews.slice(startIndex, endIndex);
  }

  changePage(page: number): void {
    if (page < 1 || (page - 1) * this.pageSize >= this.reviews.length) return;
    this.currentPage = page;
    this.paginateReviews();
  }

  searchAgent(): void {
    const query = this.searchQuery.toLowerCase().trim();
    this.filteredAgents = query
      ? this.allAgents.filter(agent => agent.agentName.toLowerCase().includes(query))
      : [];
  }

  selectAgent(agent: any): void {
    this.selectedAgent = agent;
    this.searchQuery = agent.agentName;
    this.filteredAgents = [];
    this.showWarning = false;
  }

  rate(value: number): void {
    this.newReview.rating = Number(value);
  }

  scheduleAppointment(): void {
    if (!this.selectedAgent) {
      this.showWarning = true;
      return;
    }
    this.router.navigate(['/customer/booking'], {
      queryParams: { agentId: this.selectedAgent.id }
    });
  }

  onAgentChange(): void {
    if (this.newReview.agentId) {
      this.http.get<any[]>(`http://localhost:8080/shops/agent/${this.newReview.agentId}`).subscribe(data => {
        this.shops = data;
        this.stylists = [];
        this.newReview.shopId = null;
        this.newReview.hairstylistId = null;
      });
    } else {
      this.shops = [];
      this.stylists = [];
      this.newReview.shopId = null;
      this.newReview.hairstylistId = null;
    }
  }

  onShopChange(): void {
    if (this.newReview.shopId) {
      this.http.get<any[]>(`http://localhost:8080/hairstylists/shop/${this.newReview.shopId}`).subscribe(data => {
        this.stylists = data.map(stylist => ({
          ...stylist,
          id: Number(stylist.id)
        }));
        this.newReview.hairstylistId = null;
      });
    }
  }

  formatUsername(username: string): string {
    if (!username) return '';
    if (username.length <= 2) return username[0] + '*';
    return username[0] + '*'.repeat(username.length - 2) + username[username.length - 1];
  }

  submitReview(): void {
    const customerId = this.authService.getUserId();

    if (
      !this.newReview.agentId ||
      !this.newReview.shopId ||
      !this.newReview.hairstylistId ||
      !this.newReview.rating ||
      !customerId
    ) {
      alert("❌ Please fill in the review information completely.");
      return;
    }
    const payload = {
      ...this.newReview,
      rating: Number(this.newReview.rating),
      customerId: this.customerId
    };
    this.reviewService.createReview(payload).subscribe({
      next: (created) => {
        alert('✅ Review submitted!');
        this.reviews.unshift(created);
        this.paginateReviews();
        this.calculateTopAgents();
        this.newReview = {
          rating: 0,
          comment: '',
          agentId: null,
          shopId: null,
          hairstylistId: null
        };
        this.stylists = [];
      },
      error: (err) => {
        alert('❌ Submit review failed. Please login or try again.');
        console.error('Review Submit Error:', err);
      }
    });
  }

  calculateTopAgents(): void {
    const ratingMap = new Map<number, { agentName: string, total: number, count: number }>();

    this.reviews.forEach(review => {
      if (!review.agentId || !review.agentName) return;

      if (!ratingMap.has(review.agentId)) {
        ratingMap.set(review.agentId, {
          agentName: review.agentName,
          total: 0,
          count: 0
        });
      }

      const entry = ratingMap.get(review.agentId)!;
      entry.total += review.rating;
      entry.count += 1;
    });

    this.topRatedAgents = Array.from(ratingMap.entries())
      .map(([id, value]) => ({
        agentName: value.agentName,
        avgRating: value.total / value.count,
        totalStars: value.total,
        count: value.count
      }))
      .sort((a, b) => b.avgRating - a.avgRating)
      .slice(0, 5);
  }

  loadTopStylists(): void {
    this.http.get<any[]>('http://localhost:8080/bookings/top-stylists')
      .subscribe(data => {
        this.topStylists = data;
        setTimeout(() => this.updateScrollButtons(), 500);
      });
  }
}
