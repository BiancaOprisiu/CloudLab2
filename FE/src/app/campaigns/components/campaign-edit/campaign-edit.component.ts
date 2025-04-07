import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {UntypedFormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CampaignService} from "../../services/campaign.service";
import {Campaign} from "../../models/campaign";

@Component({
  selector: 'app-campaign-edit',
  templateUrl: './campaign-edit.component.html',
  styleUrls: ['./campaign-edit.component.css']
})
export class CampaignEditComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<CampaignEditComponent>, @Inject(MAT_DIALOG_DATA) public data: Campaign, private campaignService: CampaignService, private fb: UntypedFormBuilder, private _snackBar: MatSnackBar) { }

  registerForm = this.fb.group({
    name: [this.data.name, Validators.required],
    purpose: [this.data.purpose, Validators.required]
  });

  campaignList: Campaign[];

  ngOnInit(): void {
    this.campaignService.loadCampaigns().subscribe();
    this.campaignService.getCampaigns().subscribe((camps)=>this.campaignList=camps);
  }

  onSave(campaignToUpdate: Campaign){
    if(!this.campaignList.find(camp=>camp.name===this.registerForm.get('name')?.value)) {
      campaignToUpdate.name=this.registerForm.get('name')?.value;
      campaignToUpdate.purpose=this.registerForm.get('purpose')?.value;
      campaignToUpdate.id=this.data.id;
      this.campaignService.updateCampaign(campaignToUpdate).subscribe(() => {
        this._snackBar.open("Successfully edited", "Ok", {
          duration: 3000,
          panelClass: 'ok-snack'
        });
      });

      this.dialogRef.close();
    }
    else {
      this._snackBar.open("This name already exists", "Ok", {
        duration: 3000,
        panelClass: 'error-snack'
      });
    }
  }

}
