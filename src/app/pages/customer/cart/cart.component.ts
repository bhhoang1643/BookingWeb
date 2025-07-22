import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { OrderService } from '../../../services/order.service';
import { CustomerService } from '../../../services/customer.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cart: any[] = [];
  totalAmount: number = 0;

  constructor(
    private orderService: OrderService,
    private customerService: CustomerService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const savedCart = localStorage.getItem('cart');
    if (savedCart) {
      this.cart = JSON.parse(savedCart);
    }
    this.updateTotalAmount();
  }

  updateQuantity(index: number): void {
    this.cart[index].quantity = Math.max(1, this.cart[index].quantity);
    this.updateTotalAmount();
    localStorage.setItem('cart', JSON.stringify(this.cart));
  }

  removeItem(index: number): void {
    this.cart.splice(index, 1);
    this.updateTotalAmount();
    localStorage.setItem('cart', JSON.stringify(this.cart));
  }

  updateTotalAmount(): void {
    this.totalAmount = this.cart.reduce(
      (sum, item) => sum + (item.productPrice ?? 0) * item.quantity,
      0
    );
  }

  async checkout(): Promise<void> {
    const accountId = this.authService.getAccountId();
    if (!accountId) {
      alert('❌ Bạn chưa đăng nhập!');
      return;
    }

    try {
      const customer = await this.customerService.getCustomerByAccountId(accountId).toPromise();
      if (!customer || customer.id == null) {
        alert('❌ Không tìm thấy thông tin khách hàng!');
        return;
      }

      const agentIds = [...new Set(this.cart.map(item => item.agentId))];
      if (agentIds.length !== 1) {
        alert('❌ Bạn chỉ có thể thanh toán các sản phẩm thuộc cùng một Agent!');
        return;
      }

      const agentId = agentIds[0];

      // 🔥 Tạo order với status UNPAID
      const order = {
        customerId: customer.id,
        agentId: agentId,
        totalPrice: this.totalAmount,
        paymentStatus: 'unpaid', // ✅ THÊM VÀO ĐÂY
        orderDetails: this.cart.map(item => ({
          productId: item.productId,
          quantity: item.quantity
        }))
      };

      const createdOrder = await this.orderService.createOrder(order).toPromise();
      if (!createdOrder || createdOrder.id == null) {
        alert('❌ Tạo đơn hàng thất bại!');
        return;
      }

      localStorage.removeItem('cart');
      this.cart = [];
      this.totalAmount = 0;

      this.router.navigate(['/customer/payment/order', createdOrder.id], {
        queryParams: { amount: order.totalPrice, type: 'order' }
      });

    } catch (error) {
      console.error('❌ Lỗi khi checkout:', error);
      alert('❌ Đặt hàng thất bại!');
    }
  }
}
