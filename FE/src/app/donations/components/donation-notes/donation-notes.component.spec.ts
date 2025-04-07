import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DonationNotesComponent } from './donation-notes.component';

describe('DonationNotesComponent', () => {
  let component: DonationNotesComponent;
  let fixture: ComponentFixture<DonationNotesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DonationNotesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DonationNotesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
