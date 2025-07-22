export class Account {
    constructor(
      public accountId?: number,
      public username?: string,
      public email?: string,
      public password?: string,
      public phoneNumber?: string,
      public submittedAt?: Date,
      public status?: string,
      public role?: 'CUSTOMER' | 'AGENT' | 'ADMIN'
    ) {}
  }
  