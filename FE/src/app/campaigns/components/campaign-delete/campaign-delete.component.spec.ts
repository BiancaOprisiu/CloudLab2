import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CampaignDeleteComponent } from './campaign-delete.component';

describe('CampaignDeleteComponent', () => {
  let component: CampaignDeleteComponent;
  let fixture: ComponentFixture<CampaignDeleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CampaignDeleteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CampaignDeleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
