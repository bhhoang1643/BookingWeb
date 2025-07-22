import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from '../../shared/header/header.component';
import { FooterComponent } from '../../shared/footer/footer.component';

@Component({
  selector: 'app-customer-layout',
  standalone: true,
  templateUrl: './customer-layout.component.html',
  styleUrls: ['./customer-layout.component.css'],
  imports: [RouterOutlet, HeaderComponent, FooterComponent],
})
export class CustomerLayoutComponent {}
