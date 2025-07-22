import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-vnpay-return',
  templateUrl: './vnpay-return.component.html',
  styleUrls: ['./vnpay-return.component.css']
})
export class VnpayReturnComponent implements OnInit {
  message: string = 'Processing payment...';

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    const code = this.route.snapshot.queryParamMap.get('vnp_ResponseCode');
    const txnRef = this.route.snapshot.queryParamMap.get('vnp_TxnRef');

    if (code === '00' && txnRef) {
      const params = new HttpParams()
        .set('vnp_ResponseCode', code)
        .set('vnp_TxnRef', txnRef);

      this.http.post('http://localhost:8080/payments/vnpay/callback', null, { params })
        .subscribe({
          next: () => {
            if (txnRef.startsWith('AGENTPKG_')) {
              this.message = '✅ Service package payment successful!!';
              
              localStorage.removeItem('token');
            
              setTimeout(() => {
                alert('You need to log in again to activate access.!');
                this.router.navigate(['/login']);
              }, 2000);
            } else {
              this.message = '✅ Payment successful!';
              setTimeout(() => this.router.navigate(['/customer/main']), 2000);
            }
          },
          error: err => {
            console.error(err);
            this.message = '❌ Transaction failed!';
          }
        });
    } else {
      this.message = '❌ Transaction cancelled or data incorrect!';
    }
  }
}
