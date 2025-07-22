import { TestBed } from '@angular/core/testing';

import { StyleTagService } from './style-tag.service';

describe('StyleTagService', () => {
  let service: StyleTagService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StyleTagService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
