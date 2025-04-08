import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCheckboxModule } from '@angular/material/checkbox';
import {RolePermissionsComponent} from "./components/role-permissions/role-permissions.component";
import {MatTableModule} from "@angular/material/table";
import {MatTooltipModule} from "@angular/material/tooltip";
import {RoleRoutingModule} from "./role-routing.module";


@NgModule({
  declarations: [
    RolePermissionsComponent
  ],
  imports: [
    CommonModule,
    MatCheckboxModule,
    MatTableModule,
    MatTooltipModule,
    RoleRoutingModule
  ]
})
export class RoleModule { }
