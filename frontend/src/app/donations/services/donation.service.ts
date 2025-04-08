import { Injectable } from '@angular/core';
import {BehaviorSubject, map, Observable, tap} from "rxjs";
import {Donation} from "../models/donation";
import {HttpClient} from "@angular/common/http";
import {DonationDTO} from "../models/donationDTO";
import {User} from "../../user/models/user";
import {Donor} from "../../donor/models/donor";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Campaign} from "../../campaigns/models/campaign";
import {switchMap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class DonationService {

  url:string="http://localhost:8080/donation";
  donationList$ = new BehaviorSubject<Donation[]>([]);
  donations:Donation[]
  constructor(private http:HttpClient, private _snackBar:MatSnackBar) {}

  loadDonations():Observable<Donation[]>{
    return this.http.get<Donation[]>(this.url).pipe(
      tap(donations => {this.donationList$.next(donations);})
    );
  }

  getDonations():Observable<Donation[]>{
    return this.donationList$.asObservable();
  }

  addDonation(donationDto:DonationDTO) {
    return this.http.post(this.url,donationDto);
  }

  updateDonation(donation:DonationDTO, donation_id:number): Observable<DonationDTO> {
    const updateUrl = `${this.url}/${donation_id}`;
    return this.http.put<DonationDTO>(updateUrl, donation);
  }

  deleteDonation(id: number): Observable<void> {
    const deleteUrl = `${this.url}/${id}`;
    return this.http.delete<void>(deleteUrl).pipe(
      tap(() => {
        const deleteDonationList = this.donationList$.getValue().filter(donation => donation.id !== id);
        this.donationList$.next(deleteDonationList);
      })
    );
  }

  approveDonation(donation:Donation){
    // @ts-ignore
    let currentUser:User = JSON.parse(localStorage.getItem("user"));
    if(currentUser.username == donation.createdBy.username){
      this._snackBar.open("The approver can't be the creator!","OK", {duration:3000, panelClass: 'error-snack'})
      return;
    }
    let donationToSend:DonationDTO=new DonationDTO({
      id : donation.id,
      amount : donation.amount,
      currency : donation.currency,
      campaign_id : donation.campaign_id,
      benefactor : donation.benefactor,
      notes : donation.notes
    });
    return this.http.put<DonationDTO>(`http://localhost:8080/donation/${donation.id}/approve/${currentUser.id}`,donationToSend)
  }

  hasDonations(campaign:Campaign):boolean{
    let isFound:boolean = false;
    this.getDonations().subscribe(donations =>{
      for(let donation of donations){
        if(donation.campaign_id.id == campaign.id){
          isFound = true;
        }
      }
    })
    return isFound
  }

  calculateSum(donor: Donor, currency: string, camp: Campaign): Observable<number> {
    return this.loadDonations().pipe(
      switchMap(() => this.getDonations()),
      tap(donationList => this.donations = donationList),
      map(() => {
        const donationsWithBenefactor = this.donations.filter(donation => donation.benefactor.id === donor.id && donation.campaign_id.id===camp.id);
        const totalSum = donationsWithBenefactor
          .filter(donation => donation.currency === currency && donation.approved)
          .reduce((sum, donation) => sum + donation.amount, 0);
        return totalSum;
      })
    );
  }

}
