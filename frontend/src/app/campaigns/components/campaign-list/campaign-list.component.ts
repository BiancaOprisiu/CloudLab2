import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Campaign} from "../../models/campaign";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {CampaignEditComponent} from "../campaign-edit/campaign-edit.component";
import {CampaignService} from "../../services/campaign.service";
import {BehaviorSubject, switchMap, tap} from "rxjs";
import {CampaignDeleteComponent} from "../campaign-delete/campaign-delete.component";
import {DonationService} from "../../../donations/services/donation.service";
import {DonorService} from "../../../donor/services/donor.service";
import {CampaignDonorListComponent} from "../campaign-donor-list/campaign-donor-list.component";
import {MatSnackBar} from "@angular/material/snack-bar";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-campaign-list',
  templateUrl: './campaign-list.component.html',
  styleUrls: ['./campaign-list.component.css']
})
export class CampaignListComponent implements OnInit {
  constructor(
    private donorService: DonorService,
    private campaignService: CampaignService,
    private router: Router,
    public dialog: MatDialog,
    private donationService: DonationService,
    public _snackBar: MatSnackBar,
    private translate: TranslateService
  ) {}

  displayedColumns: string[] = ['name', 'purpose', 'donors', 'edit', 'delete'];
  campaignList$: BehaviorSubject<Campaign[]> = new BehaviorSubject<Campaign[]>([]);
  dataSource: MatTableDataSource<Campaign>;
  @Output() perms = new EventEmitter<string[]>();
  roles: string[];

  ngOnInit(): void {
    this.campaignService
      .loadCampaigns()
      .pipe(
        switchMap(() => this.campaignService.getCampaigns()),
        tap((camps) => this.campaignList$.next(camps))
      )
      .subscribe();

    this.campaignList$.subscribe((camps) => {
      this.dataSource = new MatTableDataSource<Campaign>(camps);
    });

    let userDataString = localStorage.getItem('user');
    if (userDataString) {
      let userData = JSON.parse(userDataString);
      this.roles = userData.roles;
    } else {
    }
  }
  applyFilter(event: Event){
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  editCampaign(camp:Campaign ,id: number) {
    const dialogRef = this.dialog.open(CampaignEditComponent, {
      data: camp,
    });

    dialogRef.afterClosed().subscribe(result => {
      this.campaignService.loadCampaigns().subscribe();
    });
  }

  deleteCampaign(campaign: Campaign) {
    if(!this.hasDonations(campaign)) {
      const dialogRef = this.dialog.open(CampaignDeleteComponent, {
        data: campaign
      });
      dialogRef.afterClosed().subscribe(() => {
        this.campaignService.loadCampaigns().subscribe();
      });
    }
  }

  hasDonations(campaign:Campaign):boolean{
    return this.donationService.hasDonations(campaign);
  }

  openDonorList(camp: Campaign){
    const dialogRef = this.dialog.open(CampaignDonorListComponent, {
      data: camp,
    });
  }

  exportToCSV() {
    const filteredData: Campaign[] = this.dataSource.filteredData;
    if (filteredData.length === 0) {
      this._snackBar.open(
        this.translate.instant('CAMPAIGN_TABLE.NO_DATA_EXPORT'),
        'OK',
        {
          duration: 3000,
          panelClass: 'error-snack',
        }
      );
      return;
    }
    const csvData = this.generateCSVData(filteredData);
    this.downloadCSV(csvData);
  }

  generateCSVData(data: Campaign[]): string {
    const headers = [
      this.translate.instant('CAMPAIGN_TABLE.TABLE_HEADER_NAME'),
      this.translate.instant('CAMPAIGN_TABLE.TABLE_HEADER_PURPOSE'),
      this.translate.instant('CAMPAIGN_TABLE.TABLE_HEADER_DONORS'),
    ];

    const rows = data.map((campaign) => {
      const donorList = this.getDonorListForCampaign(campaign);
      return [
        campaign.name,
        campaign.purpose,
        donorList.join(', '),
      ];
    });

    const csvArray = [headers, ...rows];
    return csvArray.map((row) => row.join(',')).join('\n');
  }

  getDonorListForCampaign(campaign: Campaign): string[] {
    return campaign.donors.map(donor => (donor.firstname || '') + ' ' + (donor.lastname || '') + ' ' + (donor.additionalName || '') + ' ' + (donor.maidenName || ''));
  }

  downloadCSV(csvData: string) {
    const blob = new Blob([csvData], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'campaign_data.csv';
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
