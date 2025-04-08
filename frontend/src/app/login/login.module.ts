import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatListModule } from '@angular/material/list';
import {LoginService} from "./services/login.service";
import { MatSnackBarModule } from '@angular/material/snack-bar'
import {LoginComponent} from "./components/login/login.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatIconModule} from "@angular/material/icon";
import {LoginRoutingModule} from "./login-routing.module";
import { PasswordChangePopupComponent } from './components/password-change-popup/password-change-popup.component';
import {MatButtonModule} from "@angular/material/button";
import {MatDialogModule} from "@angular/material/dialog";
import {MatTooltipModule} from "@angular/material/tooltip";
import {TranslateModule} from "@ngx-translate/core";

@NgModule({
  declarations: [LoginComponent, PasswordChangePopupComponent],
    imports: [
        CommonModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatListModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        FormsModule,
        MatIconModule,
        LoginRoutingModule,
        MatButtonModule,
        MatDialogModule,
        MatTooltipModule,
        TranslateModule,
    ],
  providers:[
    LoginService
  ],
})
export class LoginModule { }
