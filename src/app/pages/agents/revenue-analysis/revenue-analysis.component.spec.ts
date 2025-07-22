import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RevenueAnalysisComponent } from './revenue-analysis.component';

describe('RevenueAnalysisComponent', () => {
  let component: RevenueAnalysisComponent;
  let fixture: ComponentFixture<RevenueAnalysisComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RevenueAnalysisComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RevenueAnalysisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
