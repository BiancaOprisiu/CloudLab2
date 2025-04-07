import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../../../user/services/user.service";
import {User} from "../../../user/models/user";

@Component({
  selector: 'app-not-found-page',
  templateUrl: './not-found-page.component.html',
  styleUrls: ['./not-found-page.component.css']
})
export class NotFoundPageComponent implements OnInit {

  loggedIn:boolean = false;
  constructor(private router:Router) { }

  scene = document.getElementById('scene');

  ngOnInit(): void {
    if(localStorage.getItem("user")!=undefined || localStorage.getItem("user") != null) {
      // @ts-ignore
      let user: User = JSON.parse(localStorage.getItem("user"));
      if(user != undefined){
        this.loggedIn = true;
      }
    }
  }

  goToLogin(){
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  goHome(){
    this.router.navigate(['/home/landing']);
  }
}
