import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-password-change-popup',
  templateUrl: './password-change-popup.component.html',
  styleUrls: ['./password-change-popup.component.css']
})
export class PasswordChangePopupComponent implements OnInit {

  passwordChangeForm: FormGroup;
  private _snackBar: any;

  constructor(
    public dialogRef: MatDialogRef<PasswordChangePopupComponent>,
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private authService: AuthService,
    private router: Router,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.passwordChangeForm = this.fb.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', Validators.required],
      confirmNewPassword: ['', Validators.required]
    });
  }

  ngOnInit(): void {
  }



  onChangePassword() {
    const oldPassword = this.passwordChangeForm.get('oldPassword')?.value;
    const actualOldPassword = this.data.password;
    const passwordPattern = /^(?=.*[A-Z])(?=.*\d).{6,}$/;

    if (oldPassword !== actualOldPassword) {
      this.snackBar.open('Old password doesn\'t match', 'Dismiss', {
        duration: 5000,
        panelClass: 'error-snack'
      });
    } else if (this.passwordChangeForm.get('newPassword')?.value !== this.passwordChangeForm.get('confirmNewPassword')?.value) {
      this.snackBar.open('New password doesn\'t match with the new confirmed password', 'Dismiss', {
        duration: 5000,
        panelClass: 'error-snack'
      })
    } else if (this.passwordChangeForm.get('newPassword')?.value === actualOldPassword) {
      this.snackBar.open('New password can not be old password', 'Dismiss', {
        duration: 5000,
        panelClass: 'error-snack'
      })
    }
    else if (this.passwordChangeForm.get('newPassword')?.value &&!passwordPattern.test(this.passwordChangeForm.get('newPassword')?.value)) {
      this.snackBar.open('Your password must contain at least 1 capital letter, one digit and have a length of at least 6 characters ', 'Dismiss', {
        duration: 5000,
        panelClass: 'error-snack'
      })
    }
    else {
      const username = this.data.username
      const oldPassword = this.data.password;
      const newPassword = this.passwordChangeForm.get('newPassword')?.value;
      const confirmPassword = this.passwordChangeForm.get('confirmNewPassword')?.value;
      this.authService.changePassword(username, oldPassword, newPassword, confirmPassword).subscribe()
      localStorage.setItem("loginCount", "1");
      localStorage.setItem("passwordChangeRequired", "1");
      const snackbarRef = this.snackBar.open('Thanks for changing your password. You will be redirected to login page once again', 'OK', {
        duration: 10000,
        panelClass: 'ok-snack'
      });
      localStorage.clear();

      snackbarRef.onAction().subscribe(() => {
        window.location.reload();
        localStorage.clear();
      });
    }
  }

}
