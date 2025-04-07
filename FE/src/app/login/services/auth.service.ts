import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private username: any;

  constructor(private http: HttpClient) { }

  changePassword(username: string, oldPassword: string, newPassword: string, confirmPassword: string){
    const requestBody={
      username,
      oldPassword,
      newPassword,
      confirmPassword
    };

    return this.http.post('http://localhost:8080/auth/change-password', requestBody);
  }
}
