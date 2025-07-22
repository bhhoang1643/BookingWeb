import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { JwtHelperService } from '../services/jwthelper.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterModule, CommonModule, HttpClientModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  errorMessage: string = '';

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private router: Router,
              private jwtHelper: JwtHelperService) {}

  ngOnInit(): void {
    this.createForm();
  }

  createForm() {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const { username, password } = this.loginForm.value;

      this.authService.login(username, password).subscribe({
        next: (response: any) => {
          const token = response.token;
          if (typeof response === 'string' && response.startsWith('❌')) {
            this.errorMessage = response;
            alert(response);
            return;
          }
          this.authService.saveToken(token);

          const decodedToken = this.jwtHelper.decodeToken(token);
          const role = decodedToken?.role;

          if (role === 'ADMIN') {
            this.router.navigate(['admin/manager-account']);
            alert('Login successfully with ADMIN role!');
          } else if (role === 'AGENT') {
            this.router.navigate(['/agents/dashboard']);
            alert('Login successfully with AGENT role!');
          } else if (role === 'CUSTOMER') {
            this.router.navigate(['customer/main']);
            alert('Login successfully with CUSTOMER role!');
          }
        },
        error: (err: any) => {
          this.errorMessage = 'Login failed. Please try again.!';
          console.error('Error:', err);
          alert('Login failed: ' + (err.error || 'Unknown'));
        }
      });
    } else {
      alert('Please enter complete information!');
    }
  }
}