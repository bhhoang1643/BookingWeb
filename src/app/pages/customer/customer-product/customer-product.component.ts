import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AgentService } from '../../../services/agent.service';
import { ProductService } from '../../../services/product.service';
import { Product } from '../../../common/product';
import { Agent } from '../../../common/agent';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-customer-product',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './customer-product.component.html',
  styleUrls: ['./customer-product.component.css']
})
export class CustomerProductComponent implements OnInit {
  agents: (Agent & { products: Product[] })[] = [];
  filteredAgents: (Agent & { products: Product[] })[] = [];
  searchText: string = '';
  selectedAgent: Agent | null = null;
  categoryMap: { [categoryName: string]: Product[] } = {};
  categoryNames: string[] = [];
  selectedCategory: string = '';
  filteredProducts: Product[] = [];
  constructor(
    private agentService: AgentService,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    this.agentService.getAllAgents().subscribe(agentList => {
      this.agents = [];
      agentList.forEach(agent => {
        this.productService.getProductsByAgent(agent.id!).subscribe(products => {
          const extendedAgent = { ...agent, products };
          this.agents.push(extendedAgent);
          this.filteredAgents = [...this.agents];
        });
      });
    });
  }
scrollLeft(id: string | number) {
  const container = document.getElementById(`scroll-${id}`);
  if (container) container.scrollLeft -= 300;
}

scrollRight(id: string | number) {
  const container = document.getElementById(`scroll-${id}`);
  if (container) container.scrollLeft += 300;
}


  filterAndLoadProducts(): void {
    const keyword = this.searchText.toLowerCase().trim();
    if (!keyword) {
      this.selectedAgent = null;
      this.categoryMap = {};
      return;
    }
    const found = this.agents.find(agent =>
      agent.agentName.toLowerCase().includes(keyword)
    );
    if (found) {
      this.selectedAgent = found;
      this.loadProductsGroupedByCategory(found.id!);
    } else {
      this.selectedAgent = null;
      this.categoryMap = {};
    }
  }
  

  loadProductsGroupedByCategory(agentId: number): void {
    this.productService.getProductsGroupedByCategory(agentId).subscribe(result => {
      this.categoryMap = result;
      this.categoryNames = Object.keys(result);
      this.selectedCategory = this.categoryNames[0];
      this.filteredProducts = this.categoryMap[this.selectedCategory];
    });
  }
  
  filterByCategory(category: string): void {
    this.selectedCategory = category;
    this.filteredProducts = this.categoryMap[category];
  }

  addToCart(product: Product) {
    const cart: any[] = JSON.parse(localStorage.getItem('cart') || '[]');
    const existing = cart.find(item => item.productId === product.id);

    if (existing) {
      existing.quantity += 1;
    } else {
      cart.push({
        productId: product.id!,
        quantity: 1,
        productPrice: product.price,
        name: product.name,
        image: product.image
      });
    }
    console.log('Add to cart:', product);
    alert(`✅ Added "${product.name}" to cart!`);
    localStorage.setItem('cart', JSON.stringify(cart));
  }
  
}
