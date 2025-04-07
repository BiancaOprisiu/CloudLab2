import { Component, OnInit } from '@angular/core';
import {Role} from "../../models/role";
import {Permission} from "../../../permissions/models/permission";
import {RoleService} from "../../services/role.service";
import {PermissionService} from "../../../permissions/services/permission.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {BehaviorSubject} from "rxjs";
import {switchMap, tap} from "rxjs/operators";

@Component({
  selector: 'app-role-permissions',
  templateUrl: './role-permissions.component.html',
  styleUrls: ['./role-permissions.component.css']
})
export class RolePermissionsComponent implements OnInit {

  roleList$: BehaviorSubject<Role[]> = new BehaviorSubject<Role[]>([]);
  permissionList$: BehaviorSubject<Permission[]> = new BehaviorSubject<Permission[]>([]);


  constructor(public roleService: RoleService, public permService: PermissionService, private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.roleService.loadRoles().pipe(
      switchMap(() => this.roleService.getRoles()),
      tap(roleList => this.roleList$.next(roleList))
    ).subscribe();

    this.permService.loadPermissions().pipe(
      switchMap(() => this.permService.getPermissions()),
      tap(permissionList => this.permissionList$.next(permissionList))
    ).subscribe();
  }

  hasPermission(role: Role, permission: Permission): boolean {
    return role.permisions.some(rolePermission => rolePermission.id === permission.id);
  }

  changePermission(role: Role, permission: Permission){
    if(this.hasPermission(role, permission)){
      this.roleService.deletePermissionFromRole(role, permission).subscribe(()=>{
        this.roleService.loadRoles().subscribe();
        this.permService.loadPermissions().subscribe();
      });
    }
    else{
      this.roleService.addPermissionToRole(role, permission).subscribe(()=>{
        this.roleService.loadRoles().subscribe();
        this.permService.loadPermissions().subscribe();
      });
    }
    this._snackBar.open("Successfully updated", "Ok", {
      duration: 3000,
      panelClass: 'ok-snack'
    });

  }

}
