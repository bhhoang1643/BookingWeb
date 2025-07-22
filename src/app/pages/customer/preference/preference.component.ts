import { Component, OnInit } from '@angular/core';
import { Preference } from '../../../common/preference';
import { StyleTag } from '../../../common/styletag';
import { PreferenceService } from '../../../services/preference.service';
import { StyleTagService } from '../../../services/style-tag.service';
import { AuthService } from '../../../services/auth.service';
import { Customer } from '../../../common/customer';
import { NgIf, NgFor, CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-preference',
  standalone: true,
  imports: [CommonModule, FormsModule, NgIf, NgFor],
  templateUrl: './preference.component.html',
  styleUrls: ['./preference.component.css']
})
export class PreferenceComponent implements OnInit {
  preferences: Preference[] = [];
  styleTags: StyleTag[] = [];
  selectedStyleTagId: number = 0;
  customerId!: number;

  constructor(
    private preferenceService: PreferenceService,
    private styleTagService: StyleTagService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const accountId = this.authService.getUserId();
    if (accountId === null) {
      alert('User not identified!');
      return;
    }

    this.preferenceService.getCustomerByAccountId(accountId).subscribe({
      next: (customer: Customer) => {
        this.customerId = customer.id!;
        this.loadPreferences();
        this.loadStyleTags();
      },
      error: (err: any) => {
        console.error('Customer with accountId not found:', err);
        alert('Unable to load customer data!');
      }
    });
  }

  loadPreferences(): void {
    this.preferenceService.getPreferencesByCustomerId(this.customerId).subscribe({
      next: (res: Preference[]) => this.preferences = res,
      error: (err: any) => console.error('Error while getting preferences:', err)
    });
  }

  loadStyleTags(): void {
    this.styleTagService.getAllTags().subscribe({
      next: (res: StyleTag[]) => this.styleTags = res,
      error: (err: any) => console.error('Error getting style tag:', err)
    });
  }

  addPreference(): void {
    if (this.selectedStyleTagId === 0) {
      alert('Pick a hobby!');
      return;
    }

    const newPref: Preference = {
      id: 0,
      customerId: this.customerId,
      styleTagId: this.selectedStyleTagId,
      styleTagName: ''
    };

    this.preferenceService.createPreference(newPref).subscribe({
      next: (res: Preference) => {
        this.preferences.push(res);
        this.selectedStyleTagId = 0;
      },
      error: (err: any) => alert('Preference already exists or error!')
    });
  }

  deletePreference(id: number): void {
    this.preferenceService.deletePreference(id).subscribe({
      next: () => this.preferences = this.preferences.filter(p => p.id !== id),
      error: (err: any) => console.error('Error while deleting:', err)
    });
  }
}      
   