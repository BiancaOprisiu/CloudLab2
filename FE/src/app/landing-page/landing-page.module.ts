import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LandingPageRoutingModule } from './landing-page-routing.module';
import { LandingComponent } from './components/landing/landing.component';
import {TranslateModule} from "@ngx-translate/core";


@NgModule({
  declarations: [
    LandingComponent
  ],
  imports: [
    CommonModule,
    LandingPageRoutingModule,
    TranslateModule
  ]
})
export class LandingPageModule { }
