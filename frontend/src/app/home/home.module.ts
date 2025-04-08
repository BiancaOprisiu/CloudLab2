import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {HomeComponent} from "./components/home/home.component";
import {MatIconModule} from "@angular/material/icon";
import {RouterModule} from "@angular/router";
import {HomeRoutingModule} from "./home-routing.module";
import {MatButtonModule} from "@angular/material/button";
import { NavBarComponent } from './components/nav-bar/nav-bar.component';
import {MatMenuModule} from "@angular/material/menu";
import {PermissionManagementDirective} from "../permission-manager/permission-management/permission-management.directive";
import {UserManagementDirective} from "../permission-manager/user-management/user-management.directive";
import {CampManagementDirective} from "../permission-manager/camp-management/camp-management.directive";
import {BenefManagementDirective} from "../permission-manager/benef-management/benef-management.directive";
import {DonationManagementDirective} from "../permission-manager/donation-management/donation-management.directive";
import {TranslateModule} from "@ngx-translate/core";
import {MatBadgeModule} from "@angular/material/badge";
import {PermissionManagerModule} from "../permission-manager/permission-manager.module";


@NgModule({
  declarations: [
    HomeComponent,
    NavBarComponent,
  ],
    imports: [
        CommonModule,
        RouterModule,
        MatIconModule,
        MatButtonModule,
        HomeRoutingModule,
        MatMenuModule,
        MatBadgeModule,
        TranslateModule,
      PermissionManagerModule
    ],
  exports: [
    NavBarComponent,
  ],
  providers: []
})
export class HomeModule { }
