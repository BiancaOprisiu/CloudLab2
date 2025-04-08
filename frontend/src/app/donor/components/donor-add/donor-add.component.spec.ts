import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DonorAddComponent } from './donor-add.component';

describe('DonorAddComponent', () => {
  let component: DonorAddComponent;
  let fixture: ComponentFixture<DonorAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DonorAddComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DonorAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
