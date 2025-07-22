import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { NgForOf, CurrencyPipe, CommonModule } from '@angular/common';

@Component({
  selector: 'app-agent-package',
  standalone: true,
  imports:[NgForOf, CurrencyPipe, CommonModule],
  templateUrl: './agent-package.component.html',
  styleUrls: ['./agent-package.component.css'],
})
export class AgentPackageComponent implements OnInit{

  accountId = 0;
  selectedPackage: any = null;
  packages = [
    { name: '3 month package', price: 5000000, duration: 3 },
    { name: '6 month package', price: 7000000, duration: 6 },
    { name: '12 month package', price: 12000000, duration: 12 }
  ];

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      this.accountId = payload.accountId;
    }
  }
  selectPackage(pkg: any): void {
  console.log('Package selected:', pkg); 
  this.selectedPackage = pkg;
}
  subscribe(pkg: any): void {
    if (!pkg) return;

    const token = localStorage.getItem('token');
    const requestBody = {
      accountId: this.accountId, 
      name: pkg.name,
      price: pkg.price,
      duration: pkg.duration
    };

    this.http.post<any>('http://localhost:8080/api/agent-package/subscribe', requestBody, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).subscribe({
      next: (res) => {
        const subscriptionId = res.id;
        if (confirm('Do you want to pay with VNPay?')) {
          this.router.navigate(['/fake-bank'], {
            queryParams: {
              vnp_TxnRef: `AGENTPKG_${subscriptionId}`,
              vnp_ReturnUrl: 'http://localhost:4200/vnpay-return'
            }
          });
        } else {
          this.pay(subscriptionId, 'CASH');
        }
      },
      error: (err) => {
        console.error(err);
        alert(err.error || '❌ Error while registering package');
      }
    });
  }

  pay(subscriptionId: number, method: string): void {
    const token = localStorage.getItem('token');
    this.http.post(`http://localhost:8080/payments/agent-package/${subscriptionId}?method=${method}`, {}, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).subscribe({
      next: () => {
        alert('✅ Payment successful and account activated!');
        this.router.navigate(['agents/dashboard/agent']);
      },
      error: (err) => alert('❌ Error in payment: ' + err.error)
    });
  }
}
