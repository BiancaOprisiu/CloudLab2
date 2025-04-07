import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {DonorRoutingModule} from './donor-routing.module';
import {DonorListComponent} from './components/donor-list/donor-list.component';
import {MatTableModule} from "@angular/material/table";
import {HomeModule} from "../home/home.module";
import {HttpClientModule} from "@angular/common/http";
import {MatButtonModule} from "@angular/material/button";
import {MatInputModule} from "@angular/material/input";
import {ReactiveFormsModule} from "@angular/forms";
import {DonorEditComponent} from "./components/donor-edit/donor-edit.component";
import {DonorAddComponent} from "./components/donor-add/donor-add.component";
import {MatIconModule} from "@angular/material/icon";
import {DonorDeleteComponent} from './components/donor-delete/donor-delete.component';
import {MatSelectModule} from "@angular/material/select";
import {TranslateModule} from "@ngx-translate/core";
import {BenefManagementDirective} from "../permission-manager/benef-management/benef-management.directive";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatListModule} from "@angular/material/list";
import {PermissionManagerModule} from "../permission-manager/permission-manager.module";


@NgModule({
  declarations: [
    DonorListComponent,
    DonorAddComponent,
    DonorEditComponent,
    DonorDeleteComponent,
  ],
  imports: [
    CommonModule,
    DonorRoutingModule,
    MatTableModule,
    // HomeModule,
    HttpClientModule,
    MatButtonModule,
    MatInputModule,
    ReactiveFormsModule,
    MatIconModule,
    TranslateModule,
    MatSelectModule,
    MatExpansionModule,
    MatGridListModule,
    MatListModule,
    // HomeModule
    PermissionManagerModule
  ],
  exports: [
    DonorListComponent,
  ]
})
export class DonorModule {
}
