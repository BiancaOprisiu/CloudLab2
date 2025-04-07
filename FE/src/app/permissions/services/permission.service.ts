import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, tap} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Permission} from "../models/permission";

@Injectable({
  providedIn: 'root'
})
export class PermissionService {

  url:string = 'http://localhost:8080/permisions';
  permissionList$: BehaviorSubject<Permission[]>= new BehaviorSubject<Permission[]>([]);

  constructor(private http: HttpClient) { }

  loadPermissions():Observable<Permission[]>{
    return this.http.get<Permission[]>(this.url).pipe(
      tap(perms => {this.permissionList$.next(perms)})
    );
  }

  getPermissions(): Observable<Permission[]>{
    return this.permissionList$.asObservable();
  }

  updatePermission(perm:Permission):Observable<Permission>{
    return this.http.put<Permission>(this.url+'/'+perm.id, perm);
  }

  addPermission(perm:Permission):Observable<Permission>{
    return this.http.post<Permission>(this.url, perm);
  }
}
