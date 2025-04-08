import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NotFoundRoutingModule } from './not-found-routing.module';
import {NotFoundPageComponent} from "./components/not-found-page/not-found-page.component";
import {HomeModule} from "../home/home.module";
import {LoginModule} from "../login/login.module";


@NgModule({
  declarations: [NotFoundPageComponent],
  imports: [
    CommonModule,
    NotFoundRoutingModule,
    HomeModule,
    LoginModule
  ]
})
export class NotFoundModule { }
