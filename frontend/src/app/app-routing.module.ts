import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";

const routes: Routes = [

  {
    path:'login',
    loadChildren: () =>import('./login/login.module').then(m=>m.LoginModule)
  },
  {
    path:'home',
    loadChildren:()=>import('./home/home.module').then(m=>m.HomeModule),
  },
  {
    path:'**',
    loadChildren:()=>import('./not-found/not-found.module').then(m=>m.NotFoundModule)
  },
  {
    path:'',
    redirectTo:'login',
    pathMatch:'full'
  },
];
@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forRoot(routes, {enableTracing: true}),
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }
