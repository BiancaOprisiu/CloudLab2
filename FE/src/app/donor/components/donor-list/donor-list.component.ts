import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { MatTableDataSource } from "@angular/material/table";
import { Donor } from "../../models/donor";
import { MatDialog } from "@angular/material/dialog";
import { DonorEditComponent } from "../donor-edit/donor-edit.component";
import { DonorService } from "../../services/donor.service";
import { Router } from "@angular/router";
import { DonorDeleteComponent } from "../donor-delete/donor-delete.component";
import {BehaviorSubject} from "rxjs";
import {switchMap, tap} from "rxjs/operators";

@Component({
  selector: 'app-donor-list',
  templateUrl: './donor-list.component.html',
  styleUrls: ['./donor-list.component.css']
})
export class DonorListComponent implements OnInit {

  constructor(private donorService: DonorService, private router: Router, public dialog: MatDialog, private changeDetectorRef:ChangeDetectorRef) { }

  displayedColumns: string[] = ['firstname', 'lastname', 'additionalName', 'maidenName', 'edit', 'delete'];
  donorList$: BehaviorSubject<Donor[]> = new BehaviorSubject<Donor[]>([]);
  dataSource: MatTableDataSource<Donor>;
  roles: string[];
  ngOnInit(): void {
    let userDataString = localStorage.getItem("user");
    if (userDataString) {
      let userData = JSON.parse(userDataString);
      this.roles = userData.roles;
    }
    else {
    }

    this.donorService.loadDonors().pipe(
      switchMap(() => this.donorService.getDonors()),
      tap(donors => this.donorList$.next(donors.filter((donor) => {return !(donor.lastname == "Unknown" && donor.firstname == "Unknown")})))
    ).subscribe();
    this.donorList$.subscribe(donors => {
      this.dataSource = new MatTableDataSource<Donor>(donors);
    });
  }

  editDonor(donor: Donor) {
    const dialogRef = this.dialog.open(DonorEditComponent, {
      data: donor
    });
    dialogRef.afterClosed().subscribe(() => {
      this.donorService.loadDonors().subscribe();
    });
  }

  deleteDonor(donor: Donor) {
    const dialogRef = this.dialog.open(DonorDeleteComponent, {
      data:  donor
    });
    dialogRef.afterClosed().subscribe(() => {
      this.donorService.loadDonors().subscribe();
    });
  }

  applyFilter(event: Event){
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

}
