import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { UntypedFormBuilder, UntypedFormGroup, Validators } from "@angular/forms";
import { MatSnackBar } from "@angular/material/snack-bar";
import { DonorService } from "../../services/donor.service";
import { Donor } from "../../models/donor";
import {Campaign} from "../../../campaigns/models/campaign";
import {CampaignService} from "../../../campaigns/services/campaign.service";
@Component({
  selector: 'app-donor-edit',
  templateUrl: './donor-edit.component.html',
  styleUrls: ['./donor-edit.component.css']
})
export class DonorEditComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<DonorEditComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Donor,
    private donorService: DonorService,
    private campaignService: CampaignService,
    private fb: UntypedFormBuilder,
    private _snackBar: MatSnackBar
  ) { }

  registerForm: UntypedFormGroup;
  donorList: Donor[];
  campList: Campaign[];

  ngOnInit(): void {
    this.initializeForm();
    this.campaignService.loadCampaigns().subscribe();
    this.campaignService.getCampaigns().subscribe((camps) => this.campList = camps);

  }

  private initializeForm() {
    // const initialCamps=this.data.campaigns.map(camp=>camp.name||'');
    this.registerForm = this.fb.group({
      firstname: [this.data.firstname, Validators.required],
      lastname: [this.data.lastname, Validators.required],
      additionalName: [this.data.additionalName],
      maidenName: [this.data.maidenName],
      // campaigns: [this.data.campaigns.map(camp=>camp.name||'')]
    });
  }

  onSave(donorToUpdate: Donor) {

    donorToUpdate.firstname = this.registerForm.get('firstname')?.value;
    donorToUpdate.lastname = this.registerForm.get('lastname')?.value;
    donorToUpdate.additionalName = this.registerForm.get('additionalName')?.value;
    donorToUpdate.maidenName = this.registerForm.get('maidenName')?.value;
    donorToUpdate.id=this.data.id;
    this.donorService.updateDonor(donorToUpdate).subscribe(() => {
      this._snackBar.open("Successfully edited", "Ok", {
        duration: 3000,
        panelClass: 'ok-snack'
      });
    });
    this.dialogRef.close();

  }

}
