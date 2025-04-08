import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {RolePermissionsComponent} from "./components/role-permissions/role-permissions.component";
import {RoleGuards} from "../util/role-guards";

const routes: Routes = [
  {
    path:'',
    component:RolePermissionsComponent,
    canActivate: [RoleGuards],
    data:{role:'PERMISSION_MANAGEMENT'}
  }
]

@NgModule({
  imports:[
    RouterModule.forChild(routes)
  ],
  exports:[
    RouterModule
  ]
})
export class RoleRoutingModule{}
