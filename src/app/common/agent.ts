import { Account } from "./account";

export class Agent {
  [key: string]: any;
  constructor(
    public id: number,
    public specialization: string,
    public location: string,
    public establishment: string,
    public openingHours: string,
    public professionalSkills: string,
    public ownerName: string,
    public agentName: string,
    public accountId: number,
    public account?: Account
  ) {}
}
