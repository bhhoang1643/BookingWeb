export class ServiceModel {
  constructor(
    public serviceId: number,
    public agentId: number,
    public name: string,
    public price: number,
    public status: string,
    public image: string 
  ) {}
}