import {ChangeDetectorRef, Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Donation} from "../../models/donation";
import {MatTableDataSource} from "@angular/material/table";
import {DonationService} from "../../services/donation.service";
import {Router} from "@angular/router";
import {DonorService} from "../../../donor/services/donor.service";
import {MatDialog} from "@angular/material/dialog";
import {DonationEditComponent} from "../donation-edit/donation-edit.component";
import {BehaviorSubject} from "rxjs";
import {switchMap, tap} from "rxjs/operators";
import {DonationNotesComponent} from "../donation-notes/donation-notes.component";
import {UserDetailsComponent} from "../../../user/components/user-details/user-details.component";
import {DonationDeleteComponent} from "../donation-delete/donation-delete.component";
import {User} from "../../../user/models/user";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Campaign} from "../../../campaigns/models/campaign";
import {CampaignDetailsComponent} from "../../../campaigns/components/campaign-details/campaign-details.component";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-donation-list',
  templateUrl: './donation-list.component.html',
  styleUrls: ['./donation-list.component.css']
})
export class DonationListComponent implements OnInit {

  displayedColumns: string[] = ['amount', 'currency', 'campaign', 'createdByUser', 'createdDate', 'benefactor', 'approved', 'approvedBy', 'approvedDate', 'notes', 'edit', 'delete'];
  dataSource:MatTableDataSource<Donation>;
  donationList$: BehaviorSubject<Donation[]> = new BehaviorSubject<Donation[]>([]);
  @Output() perms= new EventEmitter<string[]>()
  roles: string[];
  constructor(private donationService: DonationService,
              private changeDetectorRef: ChangeDetectorRef,
              private router:Router,
              private donorService: DonorService,
              public dialog: MatDialog,
              public _snackBar: MatSnackBar,
              private translate: TranslateService
  ) { }

  ngOnInit(): void {
    this.donationService.loadDonations().pipe(
      switchMap(() => this.donationService.getDonations()),
      tap(donationList => this.donationList$.next(donationList))
    ).subscribe();

    this.donationList$.subscribe(donationList => {
      this.dataSource = new MatTableDataSource<Donation>(donationList);
    });

    let userDataString = localStorage.getItem("user");
    if (userDataString) {
      let userData = JSON.parse(userDataString);
      this.roles = userData.roles;
    }
    else {
    }
  }

  applyFilter(event: Event){
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  editDonation(donation:Donation) {
    if(!this.isApproved(donation)) {
      const dialogRef = this.dialog.open(DonationEditComponent, {
        data: donation,
      });

      dialogRef.afterClosed().subscribe(result => {
        this.donationService.loadDonations().subscribe();
      });
    }
  }

  openNotes(donation: Donation){
    const dialogRef = this.dialog.open(DonationNotesComponent, {
      data: donation,
      maxWidth: '30%',
    });
  }

  openCreatorDetails(creator: User){
    const dialogRef = this.dialog.open(UserDetailsComponent, {
      data: creator,
    });
  }

  openCampaignDetails(camp: Campaign){
    const dialogRef = this.dialog.open(CampaignDetailsComponent, {
      data: camp,
    });
  }

  deleteDonation (donation: Donation) {
    if(!this.isApproved(donation)) {
      const dialogRef = this.dialog.open(DonationDeleteComponent, {
        data: donation
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.donationService.loadDonations().subscribe();
        }
      });
    }
  }

  approveDonation(donation:Donation){
    this.donationService.approveDonation(donation)?.subscribe(result=>{
      this._snackBar.open("Donation Approved!", "Ok", {
        duration: 3000,
        panelClass: 'ok-snack'
      });
      this.donationService.loadDonations().subscribe();
    },
      (error)=>{
      this._snackBar.open(" Donation Approved!", "Ok", {
        duration: 3000,
        panelClass: 'ok-snack'
      });
      this.donationService.loadDonations().subscribe();
    }
    );
  }

  isApproved(donation:Donation):boolean{
    return donation.approved;
  }

  exportToCSV() {
    const filteredData: Donation[] = this.dataSource.filteredData;

    if (filteredData.length === 0) {
      this._snackBar.open(
        this.translate.instant('DONATION_TABLE.NO_DATA_EXPORT'), 'OK', {
        duration: 3000,
        panelClass: 'error-snack',
      });
      return;
    }

    const csvData = this.generateCSVData(filteredData);
    this.downloadCSV(csvData);
  }

  generateCSVData(data: Donation[]): string {
    const headers = [
      this.translate.instant('DONATION_TABLE.TABLE_HEADER_AMOUNT'),
      this.translate.instant('DONATION_TABLE.TABLE_HEADER_CURRENCY'),
      this.translate.instant('DONATION_TABLE.TABLE_HEADER_CAMPAIGN'),
      this.translate.instant('DONATION_TABLE.TABLE_HEADER_CREATOR'),
      this.translate.instant('DONATION_TABLE.TABLE_HEADER_CREATION_DATE'),
      this.translate.instant('DONATION_TABLE.TABLE_HEADER_BENEFACTOR'),
      this.translate.instant('DONATION_TABLE.TABLE_HEADER_APPROVED'),
      this.translate.instant('DONATION_TABLE.TABLE_HEADER_NOTES')
    ];    const rows = data.map(donation => {
      return [
        donation.amount.toString(),
        donation.currency,
        donation.campaign_id.name,
        `${donation.createdBy.firstname} ${donation.createdBy.lastname}`,
        donation.createdDate.toString(),
        `${donation.benefactor.firstname || ''} ${donation.benefactor.lastname || ''}`,
        donation.approved,
        `${donation.notes || ''}`
      ];
    });

    const csvArray = [headers, ...rows];

    return csvArray.map(row => row.join(',')).join('\n');
  }

  downloadCSV(csvData: string) {
    const blob = new Blob([csvData], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'donation_data.csv';
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
