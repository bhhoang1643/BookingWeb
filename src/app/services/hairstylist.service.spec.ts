import { TestBed } from '@angular/core/testing';

import { HairstylistService } from './hairstylist.service';

describe('HairstylistService', () => {
  let service: HairstylistService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HairstylistService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
