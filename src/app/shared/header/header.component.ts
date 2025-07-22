import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { NgIf } from '@angular/common'; 
import { Router, RouterModule } from '@angular/router'; 
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-header',
  standalone: true, 
  imports: [NgIf, RouterModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  username: string | null = null;
  points: number | null = null;

  constructor(
    private authService: AuthService,
    private router: Router,
    private http: HttpClient
  ) {
    this.username = this.authService.getUsername();
    const accountId = this.authService.getAccountId();

    if (accountId) {
      this.http.get<any>(`http://localhost:8080/customers/by-account/${accountId}`)
        .subscribe({
          next: (data) => {
            this.points = data.point;
          },
          error: (err) => {
            console.error('❌ Cant get bonus points:', err);
          }
        });
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
