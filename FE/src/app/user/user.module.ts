import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UserListComponent} from './components/user-list/user-list.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {UserDetailsComponent} from './components/user-details/user-details.component';

import {MatButtonModule} from '@angular/material/button'
import {UserRoutingModule} from "./user-routing.module";

import {MatTableModule} from "@angular/material/table";
import { UserEditComponent } from './components/user-edit/user-edit.component';
import { UserRegisterComponent } from './components/user-register/user-register.component';

import { MatFormFieldModule } from '@angular/material/form-field';
import  { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatListModule } from '@angular/material/list';
import { MatSnackBarModule} from '@angular/material/snack-bar';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import {MatIconModule} from "@angular/material/icon";
import {HomeModule} from "../home/home.module";
import {TranslateModule} from "@ngx-translate/core";
import {UserManagementDirective} from "../permission-manager/user-management/user-management.directive";
import {DonationModule} from "../donations/donation.module";
import {PermissionManagerModule} from "../permission-manager/permission-manager.module";
@NgModule({
  declarations: [
    UserListComponent,
    UserDetailsComponent,
    UserEditComponent,
    UserRegisterComponent,
  ],
  exports: [
    UserListComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    MatButtonModule,
    UserRoutingModule,
    MatTableModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatListModule,
    MatSnackBarModule,
    MatPaginatorModule,
    MatSlideToggleModule,
    MatIconModule,
    // HomeModule,
    TranslateModule,
    // DonationModule
    PermissionManagerModule
  ]
})
export class UserModule { }
