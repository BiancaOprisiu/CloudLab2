import {NgModule} from "@angular/core";
import {LoginComponent} from "./components/login/login.component";
import {RouterModule} from "@angular/router";

const routes = [
  {
    path:'',
    component:LoginComponent
  },
  {
    path:'login',
    component: LoginComponent
  }
]

@NgModule({
  declarations:[],
  imports:[RouterModule.forChild(routes)],
  exports:[RouterModule],
})
export class LoginRoutingModule{}
