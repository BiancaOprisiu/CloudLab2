import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {DonationService} from "../../../donations/services/donation.service";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Donation} from "../../../donations/models/donation";
import {UserService} from "../../../user/services/user.service";
import {Donor} from "../../../donor/models/donor";
import {DonorService} from "../../../donor/services/donor.service";
import {CampaignService} from "../../../campaigns/services/campaign.service";
import {DonationDTO} from "../../../donations/models/donationDTO";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {BehaviorSubject} from "rxjs";
import {Campaign} from "../../../campaigns/models/campaign";
import {switchMap, tap} from "rxjs/operators";

interface Currency {
  value: string;
}

@Component({
  selector: 'app-donation-add',
  templateUrl: './donation-add.component.html',
  styleUrls: ['./donation-add.component.css'],

})
export class DonationAddComponent implements OnInit {

  donorList: Donor[];
  campaignList$:BehaviorSubject<Campaign[]> = new BehaviorSubject<Campaign[]>([]);
  benefactorList$:BehaviorSubject<Donor[]> = new BehaviorSubject<Donor[]>([]);

  searchControl: FormControl = new FormControl('');
  searchControlCampaign: FormControl = new FormControl('');
  donationForm: FormGroup = new FormGroup({
    benefactor: new FormControl(''),
    campaign: new FormControl('')
  });

  searchText: string = '';
  filteredBenefactors: any[] = [];
  filteredCampaigns: any[] = [];

  createdBy: number;
  myDate = new Date();
  currency: Currency[] = [
    {value: 'EUR'},
    {value: 'USD'},
    {value: 'RON'}
  ];

  constructor(public dialogRef: MatDialogRef<DonationAddComponent>,
              @Inject(MAT_DIALOG_DATA) public data: Donation, private fb: FormBuilder,private _snackBar: MatSnackBar, private campaignService: CampaignService, private donorService: DonorService,private userService: UserService, private donationService: DonationService, private router:Router) { }

  ngOnInit(): void {
    this.donorService.loadDonors().subscribe();
    this.donorService.getDonors().subscribe(donor=>{this.donorList=donor})
    this.resetForm();

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

  resetForm(){
    this.donationForm = this.fb.group({
      amount: [0, [Validators.required, Validators.pattern(/^[0-9]*$/)]],
      currency:['',Validators.required],
      campaign: ['', Validators.required],
      benefactor: ['', [Validators.required]],
      notes:['']
    })
  }

  onSubmit(donation:Donation) {
    const benefactor = this.donationForm.get("benefactor")?.value;
    const userDataString = localStorage.getItem("user");
    if (userDataString) {
      this.createdBy = JSON.parse(userDataString).id;
    }

    const campaign = this.donationForm.get("campaign")?.value;

    const donationRequest: DonationDTO = new DonationDTO({
      amount: this.donationForm.get("amount")?.value,
      currency: this.donationForm.get("currency")?.value,
      benefactor: { id: benefactor.id},
      createdBy: { id: this.createdBy },
      campaign_id: { id: campaign.id },
      createdDate: this.myDate,
      notes: this.donationForm.get("notes")?.value,
      approved: false,
    });

    this.donationService.addDonation(donationRequest).subscribe((value) => {
      this._snackBar.open("Successful registrations", "Ok", {
        duration: 3000,
        panelClass: 'ok-snack'
      });
      this.donationService.loadDonations().subscribe();
      this.dialogRef.close();
    });
  }

  hideSearchCamp() {
    this.searchControlCampaign.setValue('');
  }
  hideSearchBenef() {
    this.searchControl.setValue('');
  }
}
