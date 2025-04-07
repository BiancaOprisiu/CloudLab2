import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {DonationListComponent} from "./components/donation-list/donation-list.component";
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {DonationsRoutingModule} from "./donations-routing.module";
import {MatTableModule} from "@angular/material/table";
import {HomeModule} from "../home/home.module";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatIconModule} from "@angular/material/icon";
import { DonationEditComponent } from './components/donation-edit/donation-edit.component';
import {MatOptionModule} from "@angular/material/core";
import {MatSelectModule} from "@angular/material/select";
import { DonationNotesComponent } from './components/donation-notes/donation-notes.component';
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatCardModule} from "@angular/material/card";
import { DonationDeleteComponent } from './components/donation-delete/donation-delete.component';
import {DonationApproveDirective} from "../permission-manager/donation-approve/donation-approve.directive";
import {DonationReportingDirective} from "../permission-manager/donation-reporting/donation-reporting.directive";
import {TranslateModule} from "@ngx-translate/core";
import {DonationManagementDirective} from "../permission-manager/donation-management/donation-management.directive";
import {PermissionManagerModule} from "../permission-manager/permission-manager.module";

@NgModule({
  declarations: [
    DonationListComponent,
    DonationEditComponent,
    DonationNotesComponent,
    DonationDeleteComponent,
  ],
  exports: [
    DonationListComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    MatTableModule,
    DonationsRoutingModule,
    MatSlideToggleModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatOptionModule,
    MatSelectModule,
    ReactiveFormsModule,
    MatTooltipModule,
    MatCardModule,
    TranslateModule,
    // HomeModule,
    PermissionManagerModule
  ]

})
export class DonationModule { }
