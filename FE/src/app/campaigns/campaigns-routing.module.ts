import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CampaignListComponent} from "./components/campaign-list/campaign-list.component";
import {RoleGuards} from "../util/role-guards";

const routes: Routes = [{
  path:'',
  component:CampaignListComponent,
  // canActivate: [RoleGuards],
  // data:{role:'CAMP_MANAGEMENT'}
  },
  {
    path:'campaigns',
    component:CampaignListComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CampaignsRoutingModule { }
