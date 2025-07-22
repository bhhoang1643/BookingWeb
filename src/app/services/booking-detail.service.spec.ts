import { TestBed } from '@angular/core/testing';

import { BookingDetailService } from './booking-detail.service';

describe('BookingDetailService', () => {
  let service: BookingDetailService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookingDetailService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
