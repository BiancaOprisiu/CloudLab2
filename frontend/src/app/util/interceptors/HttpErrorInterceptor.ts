import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from "rxjs/operators";
import {MatStep} from "@angular/material/stepper";
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private _snackBar: MatSnackBar) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          if (Math.floor(error.status/100) == 4) {
            this._snackBar.open(error.error, "OK", {
              duration: 3000,
              panelClass: 'error-snack'
            });
          }
          if (error.status === 500) {
            this._snackBar.open(error.error, "OK", {
              duration: 5000,
              panelClass: 'error-snack'
            });
          }

          return throwError(error);
        })
      )
  }
}
