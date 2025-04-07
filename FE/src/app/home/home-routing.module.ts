import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {LoginComponent} from "../login/components/login/login.component";
import {HomeComponent} from "./components/home/home.component";
import {LandingComponent} from "../landing-page/components/landing/landing.component";
import {DonationListComponent} from "../donations/components/donation-list/donation-list.component";
import {RoleGuards} from "../util/role-guards";


const routes: Routes = [
  {
    path:'',
    component: HomeComponent,
    children:[
      {
        path:'',
        redirectTo: 'landing',
        pathMatch: 'full'
      },
      {
        path:'users',
        loadChildren: ()=>import('../user/user.module').then(m=>m.UserModule)
      },
      {
        path:'campaigns',
        loadChildren: ()=>import('../campaigns/campaigns.module').then(m=>m.CampaignsModule)
      },
      {
        path:'donors',
        loadChildren: ()=>import('../donor/donor.module').then(m=>m.DonorModule)
      },
      {
        path:'donations',
        loadChildren: ()=>import('../donations/donation.module').then(m=>m.DonationModule)
      },
      {
        path:'roles',
        loadChildren: ()=>import('../roles/role.module').then(m=>m.RoleModule)
      },
      {
        path:'register',
        loadChildren: ()=>import('../user/user.module').then(m=>m.UserModule),
      },
      {
        path:'landing',
        loadChildren: ()=>import('../landing-page/landing-page.module').then(m=>m.LandingPageModule)
      }
    ]
  },
  {
    path:'donation-add',
    loadChildren: ()=>import('../donation-add/donation-add.module').then(m=>m.DonationAddModule)
  },
  {path: 'login', component: LoginComponent},
];
@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
  ],
  exports: [
    RouterModule
  ]
})
export class HomeRoutingModule { }
