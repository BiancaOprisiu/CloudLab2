import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DonationAddComponent} from "./components/donation-add/donation-add.component";
import {RoleGuards} from "../util/role-guards";

const routes: Routes = [
  {
    path:'',
    component:DonationAddComponent,
    canActivate: [RoleGuards],
    data:{role:'DONATION_MANAGEMENT'}
  },
  {
    path:'add-donation',
    component:DonationAddComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DonationAddRoutingModule { }
