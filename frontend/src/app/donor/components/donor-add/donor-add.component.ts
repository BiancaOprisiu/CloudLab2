import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Campaign} from "../../../campaigns/models/campaign";
import {CampaignService} from "../../../campaigns/services/campaign.service";
import {UntypedFormBuilder, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Donor} from "../../models/donor";
import {DonorService} from "../../services/donor.service";

@Component({
  selector: 'app-donor-add',
  templateUrl: './donor-add.component.html',
  styleUrls: ['./donor-add.component.css']
})
export class DonorAddComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<DonorAddComponent>, @Inject(MAT_DIALOG_DATA) public data: Donor, private donorService: DonorService, private campaignService: CampaignService, private fb: UntypedFormBuilder, private _snackBar: MatSnackBar) {
  }

  registerForm = this.fb.group({
    name: ['', Validators.required],
    purpose: ['', Validators.required],
    additionalName: [''],
    maidenName: [''],
    campaigns: ['']
  });

  campList: Campaign[];
  donorList: Donor[];

  ngOnInit(): void {
    this.donorService.loadDonors().subscribe();
    this.donorService.getDonors().subscribe((donors) => this.donorList = donors);

    this.campaignService.loadCampaigns().subscribe();
    this.campaignService.getCampaigns().subscribe((camps) => this.campList = camps);
  }

  onSave() {
    const donorToAdd: Donor = {
      id: 0,
      firstname: this.registerForm.get('name')?.value,
      lastname: this.registerForm.get('purpose')?.value,
      additionalName: this.registerForm.get('additionalName')?.value,
      maidenName: this.registerForm.get('maidenName')?.value,
      campaigns: [],
    };

    this.donorService.addDonor(donorToAdd).subscribe(
      () => {
        this._snackBar.open("Successfully added", "Ok", {
          duration: 3000,
          panelClass: 'ok-snack'
        });
        this.dialogRef.close(true);
      },
      (error) => {
        console.error("Error adding donor:", error);
        this._snackBar.open("An error occurred while adding the donor", "Ok", {
          duration: 3000,
          panelClass: 'error-snack'
        });
      }
    );
  }

}
