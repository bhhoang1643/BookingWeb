export class Hairstylist {
    constructor(
      public id: number | null,
      public shopId: number,
      public name: string,
      public experience: number,
      public specialty: string,
      public image: string | null
    ) {}
  }