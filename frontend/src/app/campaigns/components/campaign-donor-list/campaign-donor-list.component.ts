import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Campaign} from "../../models/campaign";
import {Donor} from "../../../donor/models/donor";
import {DonorDeleteComponent} from "../../../donor/components/donor-delete/donor-delete.component";
import {DonorService} from "../../../donor/services/donor.service";
import {DonationService} from "../../../donations/services/donation.service";

@Component({
  selector: 'app-campaign-donor-list',
  templateUrl: './campaign-donor-list.component.html',
  styleUrls: ['./campaign-donor-list.component.css']
})
export class CampaignDonorListComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<CampaignDonorListComponent>,@Inject(MAT_DIALOG_DATA) public data: Campaign, public dialog: MatDialog, public donorService: DonorService, public donationService: DonationService) { }
  donorList: Donor[];
  selectedValue="RON";
  currencies=["RON", "EUR", "USD"]
  sum:number;

  roles: string[];

  ngOnInit(): void {
    this.donorList=this.data.donors;
    let userDataString = localStorage.getItem("user");
    if (userDataString) {
      let userData = JSON.parse(userDataString);
      this.roles = userData.roles;
    }
    else {
    }

  }



  calculateSum(donor: Donor, camp: Campaign){

      this.donationService.calculateSum(donor,  this.selectedValue, camp).subscribe(totalSum => {
        this.sum=totalSum;
    });
  }

}
