import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Donation} from "../../models/donation";
import {DonationService} from "../../services/donation.service";

@Component({
  selector: 'app-donation-delete',
  templateUrl: './donation-delete.component.html',
  styleUrls: ['./donation-delete.component.css']
})
export class DonationDeleteComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<DonationDeleteComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Donation,
    private donationService: DonationService,
    private _snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
  }

  onDelete() {
    this.donationService.deleteDonation(this.data.id).subscribe(
      () => {
        this._snackBar.open("Donation deleted successfully", "Ok", {
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

}
