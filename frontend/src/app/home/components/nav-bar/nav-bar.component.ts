import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {UserEditComponent} from "../../../user/components/user-edit/user-edit.component";
import {MatDialog} from "@angular/material/dialog";
import {UserRegisterComponent} from "../../../user/components/user-register/user-register.component";
import {UserService} from "../../../user/services/user.service";
import {CampaignAddComponent} from "../../../campaigns/components/campaign-add/campaign-add.component";
import {CampaignService} from "../../../campaigns/services/campaign.service";
import {DonorAddComponent} from "../../../donor/components/donor-add/donor-add.component";
import {DonorService} from "../../../donor/services/donor.service";
import {DonationAddComponent} from "../../../donation-add/components/donation-add/donation-add.component";
import {DonationService} from "../../../donations/services/donation.service";
import {Permission} from "../../../permissions/models/permission";
import {TranslateService} from "@ngx-translate/core";
import {NotificationService} from "../../../notifications/services/notification.service";
import {Notification} from "../../../notifications/models/notification";
import {
  NotificationDetailsComponent
} from "../../../notifications/components/notification-details/notification-details.component";
import {BehaviorSubject, delay, switchMap, tap} from "rxjs";
import {StompService} from "../../../notifications/services/websocket.service";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit, OnDestroy {

  @Output() perms= new EventEmitter<string[]>()
  constructor(private donationService: DonationService, private donorService: DonorService,
              private campaignService: CampaignService, public dialog: MatDialog, public userService: UserService,
              private translate: TranslateService) {
    translate.setDefaultLang('en');
    translate.use('en');
  }
  interval:any;
  notificationList$: Notification[] = [];

  roles: string[];
  id:number;
  unseenNr:number;
  ngOnInit(): void {
    let userDataString = localStorage.getItem("user");
    if (userDataString) {
      let userData = JSON.parse(userDataString);
      this.roles = userData.roles;
      this.id = userData.id;
    } else {
    }
    // this.reloadNotifications();
    // this.interval = setInterval(() => {this.reloadNotifications()},2000)

  }

  // reloadNotifications(){
  //   this.notificationService.loadNotifications(this.id).subscribe(notList => {
  //     this.notificationList$ = notList;
  //     this.countUnseen();
  //   });
  // }

  logout(){
    localStorage.clear();
    this.ngOnDestroy();
  }

  register(){
    const dialogRef = this.dialog.open(UserRegisterComponent);
    dialogRef.afterClosed().subscribe(() => {
      this.userService.loadUsers().subscribe();
    });
  }

  addCampaign(){
    const dialogRef = this.dialog.open(CampaignAddComponent);
    dialogRef.afterClosed().subscribe(() => {
      this.campaignService.loadCampaigns().subscribe();
    });
  }

  addDonor() {
    const dialogRef = this.dialog.open(DonorAddComponent);
    dialogRef.afterClosed().subscribe(() => {
      this.donorService.loadDonors().subscribe();
    });
  }

  addDonation(){
    const dialogRef = this.dialog.open(DonationAddComponent);
    dialogRef.afterClosed().subscribe(result => {
      this.donationService.loadDonations().subscribe();
    });
  }

  switchLanguage(lang: string): void {
    this.translate.use(lang);
  }


  // viewNotification(notification: Notification){
  //   const dialogRef = this.dialog.open(NotificationDetailsComponent,{data:notification, maxWidth: '30%'});
  //   dialogRef.afterClosed().subscribe(result => {
  //       notification.seen=true;
  //       this.notificationService.sendSeenResponse(notification);
  //       this.countUnseen();
  //   });
  //
  // }

  // countUnseen(){
  //   this.unseenNr=0;
  //   this.notificationList$.forEach((notif)=>{
  //     if(!notif.seen)
  //       this.unseenNr+=1;
  //   })
  // }

  ngOnDestroy(){
  //   clearInterval(this.interval);
  }

}
