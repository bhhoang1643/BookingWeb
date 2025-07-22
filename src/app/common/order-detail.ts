export class OrderDetail {
    id?: number;
    orderId!: number;
    productId!: number;
    quantity!: number;
    totalPrice?: number;
  
    
    productName?: string;
    productPrice?: number;
  
    constructor(orderId: number, productId: number, quantity: number) {
      this.orderId = orderId;
      this.productId = productId;
      this.quantity = quantity;
    }
  }
  