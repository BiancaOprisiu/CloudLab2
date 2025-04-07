import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NotificationsRoutingModule } from './notifications-routing.module';
import {NotificationDetailsComponent} from "./components/notification-details/notification-details.component";
import {MatLineModule} from "@angular/material/core";
import {MatListModule} from "@angular/material/list";


@NgModule({
  declarations: [NotificationDetailsComponent],
    imports: [
        CommonModule,
        NotificationsRoutingModule,
        MatLineModule,
        MatListModule
    ]
})
export class NotificationsModule { }
