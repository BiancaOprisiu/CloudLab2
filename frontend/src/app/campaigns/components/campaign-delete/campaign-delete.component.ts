import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { CampaignService } from "../../services/campaign.service";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Campaign } from "../../models/campaign";

@Component({
  selector: 'app-campaign-delete',
  templateUrl: './campaign-delete.component.html',
  styleUrls: ['./campaign-delete.component.css']
})
export class CampaignDeleteComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<CampaignDeleteComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Campaign,
    private campaignService: CampaignService,
    private _snackBar: MatSnackBar
  ) { }


  onDelete() {
    this.campaignService.deleteCampaign(this.data.id).subscribe(
      () => {
        this._snackBar.open("Campaign deleted successfully", "Ok", {
          duration: 3000,
          panelClass: 'ok-snack'
        });
      }
    );
    this.dialogRef.close();
  }

  onCancel(){
    this.dialogRef.close(false);
  }

  ngOnInit(): void {
  }
}
