import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, tap} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Role} from "../models/role";
import {Permission} from "../../permissions/models/permission";

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  url:string = 'http://localhost:8080/roles';
  roleList$: BehaviorSubject<Role[]>= new BehaviorSubject<Role[]>([]);

  constructor(private http: HttpClient) { }

  loadRoles():Observable<Role[]>{
    return this.http.get<Role[]>(this.url).pipe(
      tap(role => {this.roleList$.next(role)})
    );
  }

  getRoles(): Observable<Role[]>{
    return this.roleList$.asObservable();
  }

  updateRole(role:Role):Observable<Role>{
    return this.http.put<Role>(this.url+'/'+role.id, role);
  }

  addRole(role:Role):Observable<Role>{
    return this.http.post<Role>(this.url, role);
  }

  addPermissionToRole(role: Role, permission: Permission): Observable<any> {
    return this.http.post<any>(`${this.url}/${role.id}/permissions`, permission);
  }

  deletePermissionFromRole(role: Role, permission: Permission): Observable<any> {
    return this.http.delete<any>(`${this.url}/${role.id}/permissions/${permission.id}`);
  }

}
