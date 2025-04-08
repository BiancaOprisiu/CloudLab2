import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {Observable} from "rxjs";

@Injectable()
export class RoleGuards implements CanActivate{

  constructor(private router:Router){}
  roles:string[];
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    let userRole:string[];
    const receivedRole = route.data['role'];

    let userDataString = localStorage.getItem("user");
    if (userDataString) {
      let userData = JSON.parse(userDataString);
      this.roles = userData.roles;
    }
    else {
    }
    userRole=this.roles;

    if(userRole.find(role=>role===receivedRole)){
      return true;
    }
    else{
      this.router.navigateByUrl('/home');
      return false;
    }
  }
}
