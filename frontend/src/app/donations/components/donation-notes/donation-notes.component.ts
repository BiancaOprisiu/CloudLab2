import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Donation} from "../../models/donation";

@Component({
  selector: 'app-donation-notes',
  templateUrl: './donation-notes.component.html',
  styleUrls: ['./donation-notes.component.css']
})
export class DonationNotesComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<DonationNotesComponent>,
              @Inject(MAT_DIALOG_DATA) public data: Donation) { }

  ngOnInit(): void {
  }

}
