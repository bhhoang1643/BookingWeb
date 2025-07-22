export class Review {
    reviewId?: number;
    customerId!: number;
    agentId!: number;
    shopId!: number;
    hairstylistId?: number;
    rating!: number;
    comment!: string;
    timestamp?: Date;
  
    constructor(init?: Partial<Review>) {
      Object.assign(this, init);
    }
  }
  