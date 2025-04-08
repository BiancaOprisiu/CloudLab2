import {Component, Inject, OnInit} from '@angular/core';
import {UntypedFormBuilder, Validators} from "@angular/forms";
import {Campaign} from "../../models/campaign";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {CampaignService} from "../../services/campaign.service";
import {MatSnackBar} from "@angular/material/snack-bar";


@Component({
  selector: 'app-campaign-add',
  templateUrl: './campaign-add.component.html',
  styleUrls: ['./campaign-add.component.css']
})
export class CampaignAddComponent implements OnInit {

  constructor( public dialogRef: MatDialogRef<CampaignAddComponent>, @Inject(MAT_DIALOG_DATA) public data: Campaign, private campaignService: CampaignService, private fb: UntypedFormBuilder, private _snackBar: MatSnackBar) { }

  registerForm = this.fb.group({
    name: ['', Validators.required],
    purpose: ['', Validators.required]
  });

  campaignList: Campaign[];

  ngOnInit(): void {
    this.campaignService.loadCampaigns().subscribe();
    this.campaignService.getCampaigns().subscribe((camps)=>this.campaignList=camps);
  }

  onSave(campaignToAdd: Campaign){
    if(!this.campaignList.find(camp =>
      camp.name === this.registerForm.get('name')?.value
    )) {
      campaignToAdd.name=this.registerForm.get('name')?.value;
      campaignToAdd.purpose=this.registerForm.get('purpose')?.value;
      this.campaignService.addCampaign(campaignToAdd).subscribe(() => {
        this._snackBar.open("Successfully added", "Ok", {
          duration: 3000,
          panelClass: 'ok-snack'
        });
      });
      this.dialogRef.close();
      this.campaignService.loadCampaigns().subscribe();
    }
    else {
      this._snackBar.open("This name already exists", "Ok", {
        duration: 3000,
        panelClass: 'error-snack'
      });
      this.campaignService.loadCampaigns().subscribe();
    }
  }

}
