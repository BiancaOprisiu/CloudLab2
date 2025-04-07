import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DonorDeleteComponent } from './donor-delete.component';

describe('DonorDeleteComponent', () => {
  let component: DonorDeleteComponent;
  let fixture: ComponentFixture<DonorDeleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DonorDeleteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DonorDeleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
