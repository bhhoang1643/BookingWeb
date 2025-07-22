export class Booking {
    bookingId?: number;
    customerId!: number;
    shopId!: number;
    agentId!: number;
    datetime!: string; 
    paymentStatus!: string;
    totalPrice!: number;
  
    constructor(init?: Partial<Booking>) {
      Object.assign(this, init);
    }
  }
  