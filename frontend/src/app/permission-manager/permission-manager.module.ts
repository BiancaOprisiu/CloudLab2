import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {BenefManagementDirective} from "./benef-management/benef-management.directive";
import {CampManagementDirective} from "./camp-management/camp-management.directive";
import {CampReportingDirective} from "./camp-reporting/camp-reporting.directive";
import {DonationApproveDirective} from "./donation-approve/donation-approve.directive";
import {DonationManagementDirective} from "./donation-management/donation-management.directive";
import {DonationReportingDirective} from "./donation-reporting/donation-reporting.directive";
import {PermissionManagementDirective} from "./permission-management/permission-management.directive";
import {UserManagementDirective} from "./user-management/user-management.directive";



@NgModule({
  declarations: [
    BenefManagementDirective,
    CampManagementDirective,
    CampReportingDirective,
    DonationApproveDirective,
    DonationManagementDirective,
    DonationReportingDirective,
    PermissionManagementDirective,
    UserManagementDirective
  ],
  imports: [
    CommonModule
  ],
  exports: [
    BenefManagementDirective,
    CampManagementDirective,
    CampReportingDirective,
    DonationApproveDirective,
    DonationManagementDirective,
    DonationReportingDirective,
    PermissionManagementDirective,
    UserManagementDirective
  ]
})
export class PermissionManagerModule { }
