import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CampaignsRoutingModule } from './campaigns-routing.module';
import {CampaignListComponent} from "./components/campaign-list/campaign-list.component";
import {CampaignEditComponent} from "./components/campaign-edit/campaign-edit.component";
import {CampaignAddComponent} from "./components/campaign-add/campaign-add.component";
import {MatTableModule} from "@angular/material/table";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {CampaignDeleteComponent} from "./components/campaign-delete/campaign-delete.component";
import { CampaignDetailsComponent } from './components/campaign-details/campaign-details.component';
import {MatLineModule, MatOptionModule} from "@angular/material/core";
import {MatListModule} from "@angular/material/list";
import {CampReportingDirective} from "../permission-manager/camp-reporting/camp-reporting.directive";
import {DonationModule} from "../donations/donation.module";
import {MatTreeModule} from "@angular/material/tree";
import {MatSelectModule} from "@angular/material/select";
import {MatExpansionModule} from "@angular/material/expansion";
import { CampaignDonorListComponent } from './components/campaign-donor-list/campaign-donor-list.component';
import {TranslateModule} from "@ngx-translate/core";
import {CampManagementDirective} from "../permission-manager/camp-management/camp-management.directive";
import {MatGridListModule} from "@angular/material/grid-list";
import {DonorModule} from "../donor/donor.module";
import {BenefManagementDirective} from "../permission-manager/benef-management/benef-management.directive";
import {PermissionManagerModule} from "../permission-manager/permission-manager.module";


@NgModule({
  declarations: [
    CampaignListComponent,
    CampaignEditComponent,
    CampaignAddComponent,
    CampaignDeleteComponent,
    CampaignDetailsComponent,
    CampaignDonorListComponent,
  ],
  imports: [
    CommonModule,
    CampaignsRoutingModule,
    MatTableModule,
    MatIconModule,
    MatInputModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatLineModule,
    MatListModule,
    // DonationModule,
    MatTreeModule,
    MatOptionModule,
    MatSelectModule,
    MatExpansionModule,
    FormsModule,
    TranslateModule,
    MatGridListModule,
    // DonorModule,
    PermissionManagerModule
  ],
  exports:[
    CampaignListComponent,
    CampaignEditComponent,
  ]
})
export class CampaignsModule { }
