import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {DonationListComponent} from "./components/donation-list/donation-list.component";
import {RoleGuards} from "../util/role-guards";

export const donationRoutes: Routes = [
  {path: '',
    component:DonationListComponent,
    // canActivate: [RoleGuards],
    // data:{role:'DONATION_MANAGEMENT'}
  },
  {path: 'donations',
    component:DonationListComponent
  },
];
@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(donationRoutes)
  ],
  exports:[
    RouterModule
  ]
})
export class DonationsRoutingModule { }
