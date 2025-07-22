import { Component, OnInit } from '@angular/core';
import { Category } from '../../../common/category';
import { CategoryService } from '../../../services/category.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {
  categories: Category[] = [];
  newCategory: Category = new Category(0, '', 0);
  selectedCategory: Category | null = null;

  constructor(private categoryService: CategoryService) {}

  ngOnInit(): void {
    this.loadMyCategories();
  }

  loadMyCategories(): void {
    this.categoryService.getMyCategories().subscribe({
      next: (data) => this.categories = data,
      error: (err) => {
        console.error(err);
        alert('❌ Can not load category list!');
      }
    });
  }

  save(): void {
    if (!this.newCategory.name || this.newCategory.name.trim() === '') {
      alert('❌ Please enter category name!');
      return;
    }

    if (this.selectedCategory) {
      this.categoryService.update(this.selectedCategory.id!, this.newCategory).subscribe({
        next: () => {
          this.loadMyCategories();
          this.clearForm();
        },
        error: (err) => {
          console.error(err);
          alert('❌ Update category list false!');
        }
      });
    } else {
      this.categoryService.create(this.newCategory).subscribe({
        next: () => {
          this.loadMyCategories();
          this.clearForm();
        },
        error: (err) => {
          console.error(err);
          alert('❌ Create category false!');
        }
      });
    }
  }

  edit(cat: Category): void {
    this.selectedCategory = cat;
    this.newCategory = { ...cat };
  }

  delete(id: number): void {
    if (!id) {
      alert('❌ Can not fint ID to delete!');
      return;
    }

    this.categoryService.delete(id).subscribe({
      next: () => this.loadMyCategories(),
      error: (err) => {
        console.error(err);
        alert('❌ Delete false!');
      }
    });
  }

  clearForm(): void {
    this.newCategory = new Category(0, '', 0);
    this.selectedCategory = null;
  }
}
