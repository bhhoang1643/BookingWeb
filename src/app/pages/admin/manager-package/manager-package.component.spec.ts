import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagerPackageComponent } from './manager-package.component';

describe('ManagerPackageComponent', () => {
  let component: ManagerPackageComponent;
  let fixture: ComponentFixture<ManagerPackageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManagerPackageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManagerPackageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
