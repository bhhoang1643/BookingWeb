import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-fake-bank',
  templateUrl: './fake-bank.component.html',
  styleUrls: ['./fake-bank.component.css']
})
export class FakeBankComponent implements OnInit {
  returnUrl: string = '';
  txnRef: string = '';

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParamMap.get('vnp_ReturnUrl') || '';
    this.txnRef = this.route.snapshot.queryParamMap.get('vnp_TxnRef') || '';
  }

  confirmPayment(): void {
    const redirectUrl = `${this.returnUrl}?vnp_ResponseCode=00&vnp_TxnRef=${this.txnRef}`;
    window.location.href = redirectUrl;
  }
}
