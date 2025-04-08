import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {LoginResponse} from "../models/login-response";
import {LoginRequest} from "../models/login-request";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MatDialog} from "@angular/material/dialog";
import {Router} from "@angular/router";
@Injectable({
  providedIn: 'root'
})
export class LoginService {

  url: string='http://localhost:8080/auth/login';
  // loginResponse: BehaviorSubject<LoginResponse[]>=new BehaviorSubject<LoginResponse[]>([]);
  constructor(public http:HttpClient, private router:Router) { }

  login(loginRequest: LoginRequest){

    return this.http.post<LoginResponse>(this.url, loginRequest);
  }



}


