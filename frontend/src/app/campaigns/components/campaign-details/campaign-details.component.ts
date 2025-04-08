import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Campaign} from "../../models/campaign";

@Component({
  selector: 'app-campaign-details',
  templateUrl: './campaign-details.component.html',
  styleUrls: ['./campaign-details.component.css']
})
export class CampaignDetailsComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<CampaignDetailsComponent>,
              @Inject(MAT_DIALOG_DATA) public data: Campaign) { }

  ngOnInit(): void {
  }

}
