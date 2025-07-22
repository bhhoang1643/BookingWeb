export class Order {
  id?: number;
  customerId!: number;
  agentId!: number;         
  totalPrice?: number;
  createdAt?: Date;
  paymentStatus?: string;

  constructor(customerId: number, agentId: number) {
    this.customerId = customerId;
    this.agentId = agentId; 
  }
}
