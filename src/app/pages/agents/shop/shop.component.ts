import { Component, OnInit } from '@angular/core';
import { NgIf, NgFor, CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Shop } from '../../../common/shop';
import { ShopService } from '../../../services/shop.service';
import { AgentService } from '../../../services/agent.service';
import { AuthService } from '../../../services/auth.service';
import { Agent } from '../../../common/agent';

@Component({
  selector: 'app-shop',
  standalone: true,
  imports: [FormsModule, CommonModule, NgIf, NgFor],
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.css']
})
export class ShopComponent implements OnInit {
  shops: Shop[] = [];
  newShop: Shop = new Shop(0, 0, '', '');
  selectedShop: Shop | null = null;

  constructor(
    private shopService: ShopService,
    private agentService: AgentService,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.agentService.getMyAgent().subscribe({
      next: (agent: Agent) => {
        this.loadShopsByAgent(agent.id);
      },
      error: () => {
        alert('❌ Cannot get Agent from Account.');
      }
    });
  }

  loadShopsByAgent(agentId: number): void {
    this.shopService.getMyShops().subscribe({
      next: (data) => this.shops = data,
      error: () => alert('❌ Cannot load shops!')
    });
  }

  saveShop(): void {
   
    if (!this.newShop.location.trim() || !this.newShop.phoneNumber.trim()) {
      alert('⚠️ Please enter both Location and Phone Number.');
      return;
    }

    this.agentService.getMyAgent().subscribe({
      next: (agent: Agent) => {
        this.newShop.agentId = agent.id;

        if (this.selectedShop) {
          this.shopService.updateShop(this.selectedShop.id, this.newShop).subscribe(() => {
            this.loadShopsByAgent(agent.id);
            this.clearForm();
          });
        } else {
          this.shopService.createShop(this.newShop).subscribe(() => {
            this.loadShopsByAgent(agent.id);
            this.clearForm();
          });
        }
      },
      error: () => alert('❌ Cannot get Agent from Account!')
    });
  }

  editShop(shop: Shop): void {
    this.selectedShop = shop;
    this.newShop = { ...shop };
  }

  deleteShop(id: number): void {
    if (!id) {
      alert('❌ Cannot find shop ID!');
      return;
    }

    this.shopService.deleteShop(id).subscribe(() => {
      this.agentService.getMyAgent().subscribe(agent => {
        this.loadShopsByAgent(agent.id);
      });
    });
  }

  clearForm(): void {
    this.newShop = new Shop(0, 0, '', '');
    this.selectedShop = null;
  }
}
