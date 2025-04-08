import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DonationAddRoutingModule } from './donation-add-routing.module';
import { DonationAddComponent } from './components/donation-add/donation-add.component';
import {MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatOptionModule} from "@angular/material/core";
import {MatSelectModule} from "@angular/material/select";
import {TranslateModule} from "@ngx-translate/core";


@NgModule({
  declarations: [
    DonationAddComponent
  ],
  imports: [
    CommonModule,
    DonationAddRoutingModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatSlideToggleModule,
    FormsModule,
    MatDatepickerModule,
    MatOptionModule,
    MatSelectModule,
    TranslateModule
  ]
})
export class DonationAddModule { }
