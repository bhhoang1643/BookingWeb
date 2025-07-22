import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AccountService } from '../../../services/account.service';
import { Account } from '../../../common/account';

@Component({
  selector: 'app-manager-account',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './manager-account.component.html',
  styleUrls: ['./manager-account.component.css']
})
export class ManagerAccountComponent implements OnInit {
  accounts: Account[] = [];
  accountForm!: FormGroup;

  constructor(private accountService: AccountService, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.initForm();
    this.loadAccounts();
  }

  initForm() {
    this.accountForm = this.fb.group({
      username: [''],
      password: [''],
      email: [''],
      phoneNumber: [''],
      role: ['CUSTOMER']
    });
  }

  loadAccounts() {
    this.accountService.getAccountList().subscribe({
      next: (data) => {
        console.log('✅ Loaded accounts:', data);
        this.accounts = data;
      },
      error: (err) => {
        console.error('❌ Failed to load accounts:', err);
      }
    });
  }

  onSubmit() {
    const { username, password, email, phoneNumber, role } = this.accountForm.value;
    this.accountService.register(username, password, email, phoneNumber, role).subscribe({
      next: () => {
        alert('✅ Account registered successfully!');
        this.loadAccounts();
        this.resetForm();
      },
      error: (err) => {
        alert('❌ Register failed: ' + err.error);
      }
    });
  }

  disable(id: number) {
    if (confirm('⚠️ Are you sure you want to disable this account?')) {
      this.accountService.disableAccount(id).subscribe({
        next: () => {
          alert('✅ Account disabled successfully!');
          this.loadAccounts();
        },
        error: (err) => {
          console.error('❌ Disable error:', err);
          const errorMessage =
            err.error?.error || err.error?.message || err.error?.text || 'Unknown error';
          alert('❌ Disable failed: ' + errorMessage);
        }        
      });
    }
  }
  

  delete(id: number) {
    const confirmDelete = confirm('⚠️Are you sure you want to delete this account??');
    if (confirmDelete) {
      this.accountService.deleteAccount(id).subscribe({
        next: (res) => {
          const message = typeof res === 'string' ? res : res.message;
          alert(message || '✅ Account deleted!');
          this.loadAccounts();
        },
        error: (err) => {
          console.error('❌ Delete error:', err);
          const errorMessage = err.error?.error || err.error?.message || 'Unknown error';
          alert('❌ Delete failed: ' + errorMessage);
        }
      });
    }
  }
  

  resetForm() {
    this.accountForm.reset({
      username: '',
      password: '',
      email: '',
      phoneNumber: '',
      role: 'CUSTOMER'
    });
  }
}
