import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Customer } from '../../../common/customer';
import { CustomerService } from '../../../services/customer.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-customer-infor',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customer-infor.component.html',
  styleUrls: ['./customer-infor.component.css']
})
export class CustomerInforComponent implements OnInit {
  customer: Customer = this.createEmptyCustomer();
  selectedImageFile: File | null = null;
  defaultImage = 'https://i.imgur.com/Fw9zB3T.png';
  previewImageUrl: string = this.defaultImage;
  isExistingCustomer = false;
  readonly = false;

  constructor(
    private customerService: CustomerService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const accountId = this.authService.getUserId();
    if (!accountId) {
      alert('❌ KUnable to get user information from token!');
      return;
    }

    this.customer.accountId = +accountId;

    this.customerService.getCustomerByAccountId(accountId).subscribe({
      next: (res: Customer) => {
        this.customer = res;
        this.previewImageUrl = res.imageFile || this.defaultImage;
        this.isExistingCustomer = true;
        this.readonly = true;
      },
      error: (err) => {
        console.warn('⚠️ Customer not found, request to create new one:', err);
        this.isExistingCustomer = false;
        this.readonly = false;
        this.customer = this.createEmptyCustomer();
        this.customer.accountId = accountId;
      }
    });
  }

  private createEmptyCustomer(): Customer {
    return {
      id: 0,
      birthYear: undefined,
      gender: '',
      point: 0,
      imageFile: '',
      address: '',
      accountId: 0
    };
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (file) {
      this.selectedImageFile = file;
      const reader = new FileReader();
      reader.onload = () => {
        this.previewImageUrl = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  saveCustomer(): void {
    if (!this.customer.accountId) {
      alert('❌ User account not identified.');
      return;
    }

    const request = this.isExistingCustomer
      ? this.customerService.updateCustomer(this.customer.id!, this.customer, this.selectedImageFile || undefined)
      : this.customerService.createCustomer(this.customer, this.selectedImageFile || undefined);

    request.subscribe({
      next: (res: Customer) => {
        alert(this.isExistingCustomer ? '✅ Update successful!' : '✅ Created successfully!');
        this.customer = res;
        this.previewImageUrl = res.imageFile || this.defaultImage;
        this.isExistingCustomer = true;
        this.readonly = true;
      },
      error: (err) => {
        console.error('❌ Error saving customer information:', err);
        alert('🚫 Unable to save information!');
      }
    });
  }

  cancel(): void {
    this.readonly = true;
    this.ngOnInit();
  }

  edit(): void {
    this.readonly = false;
  }
}
