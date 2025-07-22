export class BookingDetail {
    id?: number;
    bookingId!: number;
    serviceIds!: number[];  
    stylistId!: number;
  
    constructor(init?: Partial<BookingDetail>) {
      Object.assign(this, init);
    }
  }
  