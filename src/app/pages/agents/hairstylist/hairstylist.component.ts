import { Component, OnInit } from '@angular/core';
import { Hairstylist } from '../../../common/hairstylist';
import { HairstylistService } from '../../../services/hairstylist.service';
import { ShopService } from '../../../services/shop.service';
import { Shop } from '../../../common/shop';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-hairstylist',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './hairstylist.component.html',
  styleUrls: ['./hairstylist.component.css']
})
export class HairstylistComponent implements OnInit {
  hairstylists: Hairstylist[] = [];
  shops: Shop[] = [];
  showModal = false;
  newHairstylist = new Hairstylist(null, 0, '', 0, '', null);
  selectedHairstylist: Hairstylist | null = null;
  selectedImage: File | null = null;
  selectedShopId = 0;

  constructor(
    private hairstylistService: HairstylistService,
    private shopService: ShopService
  ) {}

  ngOnInit(): void {
    this.shopService.getMyShops().subscribe({
      next: (data) => this.shops = data,
      error: () => alert('❌ Cannot load your shop list!')
    });
  }

  loadStylistsByShop(): void {
    if (!this.selectedShopId) return;
    this.hairstylistService.getHairstylistsByMyShop(this.selectedShopId).subscribe({
      next: (data) => this.hairstylists = data,
      error: () => alert('❌ Cannot load hairstylists!')
    });
  }

  onFileSelected(event: any): void {
    this.selectedImage = event.target.files[0];
  }

  save(): void {
    if (
      !this.selectedShopId ||
      !this.newHairstylist.name.trim() ||
      !this.newHairstylist.specialty.trim() ||
      this.newHairstylist.experience <= 0
    ) {
      alert('⚠️ Please fill in all required fields before saving!');
      return;
    }

    const formData = new FormData();

    if (!this.selectedHairstylist) {
      formData.append('shopId', this.selectedShopId.toString());
    }

    formData.append('name', this.newHairstylist.name);
    formData.append('experience', this.newHairstylist.experience.toString());
    formData.append('specialty', this.newHairstylist.specialty);
    if (this.selectedImage) {
      formData.append('image', this.selectedImage);
    }

    if (this.selectedHairstylist?.id != null) {
      console.log('🔁 Updating stylist ID:', this.selectedHairstylist.id);
      this.hairstylistService.updateHairstylist(this.selectedHairstylist.id, formData).subscribe({
        next: () => {
          this.loadStylistsByShop();
          this.closeModal();
        },
        error: () => alert('❌ Update failed!')
      });
    } else {
      console.log('➕ Creating new stylist...');
      this.hairstylistService.createHairstylist(formData).subscribe({
        next: () => {
          this.loadStylistsByShop();
          this.closeModal();
        },
        error: () => alert('❌ Create failed!')
      });
    }
  }

  edit(h: Hairstylist): void {
    this.selectedHairstylist = { ...h };
    this.newHairstylist = { ...h };
    this.selectedImage = null;
    console.log('✏️ Editing stylist ID:', h.id);
    this.showModal = true;
  }

  delete(id: number | undefined): void {
    if (!id) {
      alert('❌ ID not found!');
      return;
    }
    this.hairstylistService.deleteHairstylist(id).subscribe(() => {
      this.loadStylistsByShop();
    });
  }

  clearForm(): void {
    this.newHairstylist = new Hairstylist(null, this.selectedShopId, '', 0, '', null);
    this.selectedHairstylist = null;
    this.selectedImage = null;
  }

  openModal(): void {
    this.clearForm();
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.clearForm();
  }
}
