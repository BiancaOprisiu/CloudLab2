import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { DonorService } from "../../services/donor.service";
import { MatSnackBar } from "@angular/material/snack-bar";
import {Donor} from "../../models/donor";

@Component({
  selector: 'app-donor-delete',
  templateUrl: './donor-delete.component.html',
  styleUrls: ['./donor-delete.component.css']
})
export class DonorDeleteComponent {

  constructor(
    public dialogRef: MatDialogRef<DonorDeleteComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Donor,
    private donorService: DonorService,
    private _snackBar: MatSnackBar
  ) { }

  onDelete() {
    this.donorService.deleteDonor(this.data.id).subscribe(
      () => {
        this._snackBar.open("Donor deleted successfully", "Ok", {
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
