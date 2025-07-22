import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgentPackageComponent } from './agent-package.component';

describe('AgentPackageComponent', () => {
  let component: AgentPackageComponent;
  let fixture: ComponentFixture<AgentPackageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgentPackageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AgentPackageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
