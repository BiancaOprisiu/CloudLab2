import { Component, OnInit } from '@angular/core';
import {UntypedFormBuilder, Validators} from "@angular/forms";
import {LoginRequest} from "../../models/login-request";
import {LoginService} from "../../services/login.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {PasswordChangePopupComponent} from "../password-change-popup/password-change-popup.component";
import {AuthService} from "../../services/auth.service";
import {LoginResponse} from "../../models/login-response";
import {UserService} from "../../../user/services/user.service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  })
  private passwordChangeForm: any;

  private baseUrl = 'http://localhost:8080/users';

  constructor(private fb: UntypedFormBuilder, private loginService: LoginService, private _snackBar: MatSnackBar, private router: Router, private dialog: MatDialog, private authService:AuthService, private http: HttpClient, private userService: UserService) {
  }

  ngOnInit(): void {

  }

  onLogin() {

    const username = this.loginForm.get('username')?.value;
    const password = this.loginForm.get('password')?.value;

    const loginRequest = new LoginRequest(username, password);

    this.loginService.login(loginRequest).subscribe((loginResponse:LoginResponse)=>{
      localStorage.setItem("token", loginResponse.accessToken);
      localStorage.setItem('user', JSON.stringify(loginResponse));
      localStorage.setItem('role', 'admin');
      localStorage.setItem('loginCount',loginResponse.loginCount.toString());
      localStorage.setItem('passwordChangeRequired',String(loginResponse.passwordChangeRequired.valueOf()));

      const loginCountString = localStorage.getItem("loginCount");

      const loginPasswordChangeRequired = localStorage.getItem("passwordChangeRequired")


      var passwordChangeRequired;

      if (loginCountString !== null) {
        const loginCount = parseInt(loginCountString);

        if (loginPasswordChangeRequired !== null) {
          if(loginPasswordChangeRequired === 'false') {
            passwordChangeRequired = 0
          }
          else
          {
            passwordChangeRequired = 1
          }

          if (isNaN(loginCount)) {
            this.router.navigate(['/home/landing']);
            // Handle the case where loginCount is not a valid number
          } else {
            //
            if (loginCount === 0)

            {
              const dialogRef = this.dialog.open(PasswordChangePopupComponent, {
                width: '20%',
                data: {"password":password,"username":username}
              });

              dialogRef.afterClosed().subscribe(result => {
              });


            } else if (loginCount === 1 && passwordChangeRequired === 1) {
              this.router.navigate(['/home/landing']);

            }

            else{
              {
                const dialogRef = this.dialog.open(PasswordChangePopupComponent, {
                  width: '20%',
                  data: {"password":password,"username":username}
                });

                dialogRef.afterClosed().subscribe(result => {
                });
              }
            }
          }
        }
      }
    });
  }
}

