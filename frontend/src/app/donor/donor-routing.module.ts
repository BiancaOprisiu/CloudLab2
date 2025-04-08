import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DonorListComponent} from "./components/donor-list/donor-list.component";
import {RoleGuards} from "../util/role-guards";

const routes: Routes = [
  {path:'',
    component:DonorListComponent,
    // canActivate: [RoleGuards],
    // data:{role:'BENEF_MANAGEMENT'}
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DonorRoutingModule { }
