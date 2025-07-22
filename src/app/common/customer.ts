import { Account } from './account';

export class Customer {
  id?: number;
  birthYear?: Date;
  gender?: string;
  point?: number;
  imageFile?: string;
  address?: string;            
  accountId?: number;
  account?: Account;           
}
