import { Component, OnInit } from '@angular/core';
import { Product } from '../../../common/product';
import { Category } from '../../../common/category';
import { ProductService } from '../../../services/product.service';
import { CategoryService } from '../../../services/category.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {
  products: Product[] = [];
  filteredProducts: Product[] = [];
  categories: Category[] = [];
  selectedCategoryId: number = 0;
  formInvalid: boolean = false;
  newProduct: Product = new Product(0, '', 0, '', 0, 0, '');
  selectedFile: File | null = null;
  editing: boolean = false;
  showModal: boolean = false;

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService
  ) {}

  ngOnInit(): void {
    this.loadProducts();
    this.loadCategories();
  }

  loadProducts(): void {
    this.productService.getMyProducts().subscribe({
      next: (data) => {
        this.products = data;
        this.filterByCategory();
      },
      error: (err) => {
        console.error(err);
        alert('❌ Unable to load product list!');
      }
    });
  }

  loadCategories(): void {
    this.categoryService.getMyCategories().subscribe({
      next: (data) => this.categories = data,
      error: (err) => {
        console.error(err);
        alert('❌ Unable to load category list!');
      }
    });
  }

  filterByCategory(): void {
    if (this.selectedCategoryId === 0) {
      this.filteredProducts = this.products;
    } else {
      this.filteredProducts = this.products.filter(p => p.categoryId === this.selectedCategoryId);
    }
  }

  getCategoryName(categoryId: number): string {
    const category = this.categories.find(c => c.id === categoryId);
    return category ? category.name : 'unkow';
  }

  openModal(): void {
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.resetForm();
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  save(): void {
    if (!this.newProduct.name.trim() || !this.newProduct.price || !this.newProduct.categoryId) {
      this.formInvalid = true;
      return;
    }
    this.formInvalid = false;

    if (this.editing) {
      this.productService.updateProduct(this.newProduct.id, this.newProduct, this.selectedFile).subscribe({
        next: () => {
          this.loadProducts();
          this.closeModal();
        },
        error: (err) => {
          console.error(err);
          alert('❌ Product update failed!');
        }
      });
    } else {
      this.productService.createProduct(this.newProduct, this.selectedFile).subscribe({
        next: () => {
          this.loadProducts();
          this.closeModal();
        },
        error: (err) => {
          console.error(err);
          alert('❌ Create new failed product!');
        }
      });
    }
  }

  edit(product: Product): void {
    this.newProduct = { ...product };
    this.selectedFile = null;
    this.editing = true;
    this.showModal = true;
  }

  delete(id: number): void {
    if (confirm('Are you sure you want to delete this product??')) {
      this.productService.deleteProduct(id).subscribe({
        next: () => this.loadProducts(),
        error: (err) => {
          console.error(err);
          alert('❌ Delete product failed!');
        }
      });
    }
  }

  resetForm(): void {
    this.newProduct = new Product(0, '', 0, '', 0, 0, '');
    this.selectedFile = null;
    this.editing = false;
    this.formInvalid = false;
  }
}
