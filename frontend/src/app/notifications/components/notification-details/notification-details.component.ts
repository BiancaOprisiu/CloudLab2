import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Notification} from "../../models/notification"
import {NotificationService} from "../../services/notification.service";

@Component({
  selector: 'app-notification-details',
  templateUrl: './notification-details.component.html',
  styleUrls: ['./notification-details.component.css']
})
export class NotificationDetailsComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<NotificationDetailsComponent>, @Inject(MAT_DIALOG_DATA) public data: Notification, private notificationService:NotificationService) { }

  ngOnInit(): void {
  }

}
