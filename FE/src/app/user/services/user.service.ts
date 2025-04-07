import { Injectable } from '@angular/core';
import {User} from "../models/user";
import {BehaviorSubject, catchError, Observable, of, tap} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  url:string = 'http://localhost:8080/users';
  userList$: BehaviorSubject<User[]>= new BehaviorSubject<User[]>([]);

  constructor(private http: HttpClient) { }

  loadUsers():Observable<User[]>{
    return this.http.get<User[]>(this.url).pipe(
      tap(users => {this.userList$.next(users)})
    );
  }

  getUsers(): Observable<User[]>{
    return this.userList$.asObservable();
  }

  updateUser(user:User):Observable<User>{
    //@ts-ignore
    let userdata=JSON.parse(localStorage.getItem("user"))
    return this.http.put<User>(this.url+'/'+user.id+'/'+userdata.id, user);
  }

  activate(user:User):Observable<User>{
    return this.http.put<User>(this.url+'/'+user.id+'/activate', user);
  }

  deactivate(user:User):Observable<User>{
    return this.http.put<User>(this.url+'/'+user.id+'/deactivate', user);
  }

  addUser(user:User):Observable<User>{
    return this.http.post<User>(this.url, user);
  }

  getUserById(userId: number): Observable<User> {
    const url = `${this.url}/${userId}`;
    return this.http.get<User>(url);
  }

  getUserByUsername(userName: string): Observable<User> {
    const url = `${this.url}/search?username=${userName}`;
    return this.http.get<User>(url);
  }
}
