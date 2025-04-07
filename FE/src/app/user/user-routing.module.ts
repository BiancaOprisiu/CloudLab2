import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {UserListComponent} from "./components/user-list/user-list.component";
import {UserRegisterComponent} from "./components/user-register/user-register.component";
import {RoleGuards} from "../util/role-guards";

export const userRoutes: Routes = [
  { path: '',
    component: UserListComponent,
    // canActivate: [RoleGuards],
    // data:{role:'USER_MANAGEMENT'}
  },
  {path:'register', component:UserRegisterComponent}
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(userRoutes)
  ],
  exports:
  [
    RouterModule
  ]
})
export class UserRoutingModule { }
