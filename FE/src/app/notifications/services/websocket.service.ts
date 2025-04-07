import {Injectable} from "@angular/core";
import * as SockJS from 'sockjs-client'
import * as Stomp from 'stompjs'

@Injectable({
  providedIn:'root',
  })
export class StompService{
  socket = new SockJS('http://localhost:8080/notifications');
  stompClient = Stomp.over(this.socket);

  // subscribe(topic:string, callback:any){
  //   const connected: boolean = this.stompClient.connected;
  //   if(connected){
  //     this.subscribeToTopic(topic, callback);
  //   }
  //   this.stompClient.connect({}, () =>{
  //     this.subscribeToTopic(topic, callback);
  //   })
  // }
  //
  // private subscribeToTopic(topic: string, callback:any){
  //   this.stompClient.subscribe(topic, ():any => {
  //      callback();
  //   })
  // }
}



