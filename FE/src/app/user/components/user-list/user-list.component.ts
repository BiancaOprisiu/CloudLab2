import { Component, OnInit } from '@angular/core';
import { UserService } from "../../services/user.service";
import { MatTableDataSource } from "@angular/material/table";
import { MatDialog } from "@angular/material/dialog";
import { MatSnackBar } from "@angular/material/snack-bar";
import { BehaviorSubject } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { UserEditComponent } from "../user-edit/user-edit.component";
import { User } from '../../models/user';
import { Router } from "@angular/router";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  displayedColumns: string[] = ['firstname', 'lastname', 'username', 'email', 'mobilenumber', 'active', 'edit'];

  userList$: BehaviorSubject<User[]> = new BehaviorSubject<User[]>([]);
  dataSource: MatTableDataSource<User>;

  roles: string[];

  constructor(private userService: UserService, private router: Router, public dialog: MatDialog, private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    let userDataString = localStorage.getItem("user");
    if (userDataString) {
      let userData = JSON.parse(userDataString);
      this.roles = userData.roles;
    }
    else {
    }

    this.userService.loadUsers().pipe(
      switchMap(() => this.userService.getUsers()),
      tap(userList => this.userList$.next(userList))
    ).subscribe();

    this.userList$.subscribe(userList => {
      this.dataSource = new MatTableDataSource<User>(userList);
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  editUser(user: User) {
    const dialogRef = this.dialog.open(UserEditComponent, {
      data: user,
    });
    dialogRef.afterClosed().subscribe(() => {
      this.userService.loadUsers().subscribe();
    });
  }

  changeActiveStatus(user: User) {
    user.active = !user.active;
    const activateAction$ = user.active ? this.userService.activate(user) : this.userService.deactivate(user);

    activateAction$.pipe(
      switchMap(() => this.userService.loadUsers()),
      tap(updatedUserList => this.userList$.next(updatedUserList)),
      tap(() => {
        const message = user.active ? "Successfully activated" : "Successfully deactivated";
        const duration = 3000;
        this._snackBar.open(message, "Ok", { duration });
      })
    ).subscribe();
  }

  navigateToUserDetails(id: number) {
    this.router.navigate(['/users/edit'], { queryParams: { id: id } });
  }
}
