import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FakeBankComponent } from './fake-bank.component';

describe('FakeBankComponent', () => {
  let component: FakeBankComponent;
  let fixture: ComponentFixture<FakeBankComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FakeBankComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FakeBankComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
