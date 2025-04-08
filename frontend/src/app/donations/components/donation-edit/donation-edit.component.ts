import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Donation} from "../../models/donation";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CampaignService} from "../../../campaigns/services/campaign.service";
import {DonationService} from "../../services/donation.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DonationDTO} from "../../models/donationDTO";
import {BehaviorSubject} from "rxjs";
import {Campaign} from "../../../campaigns/models/campaign";
import {Donor} from "../../../donor/models/donor";
import {switchMap, tap} from "rxjs/operators";
import {DonorService} from "../../../donor/services/donor.service";

interface Currency {
  value: string;
}
@Component({
  selector: 'app-donation-edit',
  templateUrl: './donation-edit.component.html',
  styleUrls: ['./donation-edit.component.css']
})
export class DonationEditComponent implements OnInit {

  donorList: Donor[];
  campaignList$:BehaviorSubject<Campaign[]> = new BehaviorSubject<Campaign[]>([]);
  benefactorList$:BehaviorSubject<Donor[]> = new BehaviorSubject<Donor[]>([]);

  searchControl: FormControl = new FormControl('');
  searchControlCampaign: FormControl = new FormControl('');
  donationEditForm: FormGroup = new FormGroup({
    benefactor: new FormControl(''),
    campaignId: new FormControl('')
  });

  searchText: string = '';
  filteredBenefactors: any[] = [];
  filteredCampaigns: any[] = [];

  currency: Currency[] = [
    {value: 'EUR'},
    {value: 'USD'},
    {value: 'RON'}
  ];

  constructor(public dialogRef: MatDialogRef<DonationEditComponent>,
              @Inject(MAT_DIALOG_DATA) public data: Donation, private fb: FormBuilder,private _snackBar: MatSnackBar, private donationService: DonationService, private campaignService: CampaignService, private donorService: DonorService) { }

  ngOnInit(): void {
    this.donorService.loadDonors().subscribe();
    this.donorService.getDonors().subscribe(donor=>{this.donorList=donor})
    this.initForm();

    this.campaignService.loadCampaigns().pipe(
      switchMap(() => this.campaignService.getCampaigns()),
      tap(camps => this.campaignList$.next(camps))
    ).subscribe();

    this.donorService.loadDonors().pipe(
      switchMap(() => this.donorService.getDonors()),
      tap(donors => this.benefactorList$.next(donors))
    ).subscribe();

    this.benefactorList$.subscribe(benefactors => {
      this.filteredBenefactors = benefactors;
    });

    this.searchControl.valueChanges.subscribe(searchText => {
      this.searchText = searchText;
      this.filterBenefactors();
    });

    this.campaignList$.subscribe(campaigns => {
      this.filteredCampaigns = campaigns;
    });

    this.searchControlCampaign.valueChanges.subscribe(searchText => {
      this.searchText = searchText;
      this.filterCampaigns();
    });
  }

  filterBenefactors() {
    const searchText = this.searchText.toLowerCase();
    this.filteredBenefactors = this.benefactorList$.value.filter(benefactor =>
      (benefactor.firstname || '').toLowerCase().includes(searchText) ||
      (benefactor.lastname || '').toLowerCase().includes(searchText) ||
      (benefactor.maidenName || '').toLowerCase().includes(searchText) ||
      (benefactor.additionalName || '').toLowerCase().includes(searchText)
    );
  }

  filterCampaigns() {
    const searchText = this.searchText.toLowerCase();
    this.filteredCampaigns = this.campaignList$.value.filter(campaign =>
      campaign.name.toLowerCase().includes(searchText)
    );
  }

  initForm(){

    this.donationEditForm = this.fb.group({
      amount: [this.data.amount, [Validators.required, Validators.pattern(/^[0-9]*$/)]],
      currency: [this.data.currency, Validators.required],
      campaignId: [this.data.campaign_id.id, [Validators.required]],
      createdDate: [this.data.createdDate, Validators.required],
      benefactor: [this.data.benefactor.id, [Validators.required]],
      notes: [this.data.notes]
    })
  }

  onSave() {
    let benefactorValue = this.donationEditForm.get('benefactor')?.value;
    let campaignValue = this.donationEditForm.get('campaignId')?.value;

    const benefactorId = typeof benefactorValue === 'object' ? benefactorValue.id : benefactorValue;
    const campaignId = typeof campaignValue === 'object' ? campaignValue.id : campaignValue;

    let donationToUpdate:DonationDTO = new DonationDTO({
      id : this.data.id,
      amount : this.donationEditForm.get('amount')?.value,
      currency : this.donationEditForm.get('currency')?.value,
      campaign_id: { id: campaignId },
      benefactor: { id: benefactorId },
      notes : this.donationEditForm.get('notes')?.value
    });

    this.donationService.updateDonation(donationToUpdate, this.data.id).subscribe( () => {
      this._snackBar.open("Edit successful", "Ok", {
        duration: 3000,
        panelClass: 'ok-snack'
      });
      this.donationService.loadDonations().subscribe();
    });
    this.dialogRef.close();
  }

  hideSearchCamp() {
    this.searchControlCampaign.setValue('');
  }
  hideSearchBenef() {
    this.searchControl.setValue('');
  }
}
