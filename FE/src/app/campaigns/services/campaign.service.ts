import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, of, tap} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Campaign} from "../models/campaign";

@Injectable({
  providedIn: 'root'
})
export class CampaignService {

  url:string = 'http://localhost:8080/campaigns';

  constructor(private http: HttpClient) { }

  getCampaignById(campaignId: number): Observable<Campaign> {
    const url = `${this.url}/${campaignId}`;
    return this.http.get<Campaign>(url);
  }

  campaignList$: BehaviorSubject<Campaign[]>= new BehaviorSubject<Campaign[]>([]);

  loadCampaigns():Observable<Campaign[]>{
    return this.http.get<Campaign[]>(this.url).pipe(
      tap(camps => {
        this.campaignList$.next(camps);
      })
    );
  }

  getCampaigns(): Observable<Campaign[]>{
    return this.campaignList$.asObservable();
  }

  updateCampaign(camp:Campaign):Observable<Campaign>{
    return this.http.put<Campaign>(this.url+'/'+camp.id, camp);
  }

  addCampaign(camp:Campaign):Observable<Campaign>{
    return this.http.post<Campaign>(this.url, camp);
  }

  deleteCampaign(id: number): Observable<void> {
    const deleteUrl = `${this.url}/${id}`;
    return this.http.delete<void>(deleteUrl).pipe(
      tap(() => {
        const updatedDonorList = this.campaignList$.getValue().filter(campaign => campaign.id !== id);
        this.campaignList$.next(updatedDonorList);
      })
    );
  }
}
