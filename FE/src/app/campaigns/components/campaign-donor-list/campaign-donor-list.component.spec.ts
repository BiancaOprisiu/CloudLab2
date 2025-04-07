import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CampaignDonorListComponent } from './campaign-donor-list.component';

describe('CampaignDonorListComponent', () => {
  let component: CampaignDonorListComponent;
  let fixture: ComponentFixture<CampaignDonorListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CampaignDonorListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CampaignDonorListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
