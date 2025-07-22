import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../../services/product.service';
import { Product } from '../../../common/product';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, FormsModule], 
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {
  product: Product = new Product(0, '', 0, '', 0, 0, '');
  quantity: number = 1;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getProductPublic(id).subscribe(data => {
      this.product = data;
    });
  }

  increaseQty() {
    this.quantity++;
  }

  decreaseQty() {
    if (this.quantity > 1) this.quantity--;
  }

  addToCart(product: Product) {
    const cart: any[] = JSON.parse(localStorage.getItem('cart') || '[]');
    const existing = cart.find(item => item.productId === product.id);
    if (existing) {
      existing.quantity += this.quantity;
    } else {
      cart.push({
        productId: product.id,
        name: product.name,
        image: product.image,
        productPrice: product.price,
        quantity: this.quantity
      });
    }
    console.log('Add to cart:', product);
    alert(`✅ Add "${product.name}" to cart!`);
    localStorage.setItem('cart', JSON.stringify(cart));
  }
}
