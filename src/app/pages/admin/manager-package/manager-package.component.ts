import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PackageService } from '../../../services/package.service';
import { AgentPackage } from '../../../common/agent-package';

@Component({
  selector: 'app-manager-package',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './manager-package.component.html',
  styleUrls: ['./manager-package.component.css']
})
export class ManagerPackageComponent implements OnInit {
  packages: AgentPackage[] = [];
  packageForm!: FormGroup;
  isEditing: boolean = false;
  editId?: number;
  showForm: boolean = false;

  constructor(
    private packageService: PackageService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadPackages();
  }

  initForm() {
    this.packageForm = this.fb.group({
      name: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(0)]],
      duration: [1, [Validators.required, Validators.min(1)]],
      accountId: [1, Validators.required],
      paymentStatus: ['unpaid', Validators.required] 
    });
  }

  loadPackages() {
    this.packageService.getAllPackages().subscribe({
      next: (data) => {
        this.packages = data;
      },
      error: (err) => {
        console.error('❌ Load package error:', err);
      }
    });
  }

  showCreateForm() {
    this.showForm = true;
    this.isEditing = false;
    this.packageForm.reset({
      name: '',
      price: 0,
      duration: 1,
      accountId: 1,
      paymentStatus: 'unpaid'
    });
  }

  cancelForm() {
    this.showForm = false;
    this.isEditing = false;
    this.resetForm();
  }

  onSubmit() {
    if (this.packageForm.invalid) return;

    if (this.isEditing && this.editId) {
      const updateData = {
        name: this.packageForm.value.name,
        price: this.packageForm.value.price,
        duration: this.packageForm.value.duration,
        paymentStatus: this.packageForm.value.paymentStatus 
      };
      this.packageService.updatePackage(this.editId, updateData).subscribe({
        next: () => {
          alert('✅ Cập nhật gói thành công!');
          this.loadPackages();
          this.resetForm();
          this.showForm = false;
        },
        error: (err) => {
          console.error('❌ Update package error:', err);
          alert('❌ Lỗi cập nhật gói');
        }
      });
    } else {
      this.packageService.createPackage(this.packageForm.value).subscribe({
        next: () => {
          alert('✅ Tạo gói mới thành công!');
          this.loadPackages();
          this.resetForm();
          this.showForm = false;
        },
        error: (err) => {
          console.error('❌ Create package error:', err);
          alert('❌ Lỗi tạo gói mới');
        }
      });
    }
  }

  edit(pkg: AgentPackage) {
    this.isEditing = true;
    this.editId = pkg.id;
    this.showForm = true;
    this.packageForm.patchValue({
      name: pkg.name,
      price: pkg.price,
      duration: this.getDuration(pkg.startDate, pkg.endDate),
      accountId: pkg.accountId,
      paymentStatus: pkg.paymentStatus 
    });
  }

  delete(id: number) {
    if (confirm('⚠️ Bạn có chắc chắn muốn xóa gói này không?')) {
      this.packageService.deletePackage(id).subscribe({
        next: () => {
          alert('✅ Xóa gói thành công!');
          this.loadPackages();
        },
        error: (err) => {
          console.error('❌ Delete package error:', err);
          alert('❌ Lỗi xóa gói');
        }
      });
    }
  }

  resetForm() {
    this.packageForm.reset({
      name: '',
      price: 0,
      duration: 1,
      accountId: 1,
      paymentStatus: 'unpaid'
    });
    this.isEditing = false;
    this.editId = undefined;
  }

  private getDuration(start: string, end: string): number {
    const startDate = new Date(start);
    const endDate = new Date(end);
    const months = (endDate.getFullYear() - startDate.getFullYear()) * 12 +
                   (endDate.getMonth() - startDate.getMonth());
    return months;
  }
}
