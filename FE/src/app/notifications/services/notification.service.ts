import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Notification} from "../models/notification";
import {BehaviorSubject, Observable, tap} from "rxjs";
import {StompService} from "./websocket.service";

@Injectable({
  providedIn: 'root',
})
export class NotificationService implements OnInit{

  url1$:string = 'http://localhost:8080/users/';
  url2$:string = 'http://localhost:8080/notifications';
  notificationList$: BehaviorSubject<Notification[]> = new BehaviorSubject<Notification[]>([]);

  constructor(private http: HttpClient) { }

  ngOnInit() {
  }
  //
  // loadNotifications(id:number): Observable<Notification[]>{
  //   return this.http.get<Notification[]>(this.url1$+id.toString()+"/notifications").pipe(
  //     tap(notifications => {
  //       this.notificationList$.next(notifications.reverse());
  //     })
  //   );
  // }
  //
  // sendSeenResponse(notification: Notification){
  //   return this.http.put("http://localhost:8080/seen", notification).subscribe();
  // }

}
