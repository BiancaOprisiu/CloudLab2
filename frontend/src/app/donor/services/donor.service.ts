import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Donor } from '../models/donor';

@Injectable({
  providedIn: 'root'
})
export class DonorService {
  url: string = 'http://localhost:8080/donors';
  donorList$: BehaviorSubject<Donor[]> = new BehaviorSubject<Donor[]>([]);

  constructor(private http: HttpClient) {}

  loadDonors(): Observable<Donor[]> {
    return this.http.get<Donor[]>(this.url).pipe(
      tap(donors => {
        this.donorList$.next(donors);
      })
    );
  }

  getDonors(): Observable<Donor[]> {
    return this.donorList$.asObservable();
  }

  addDonor(donor: Donor) {
    return this.http.post(this.url, donor);
  }

  updateDonor(donor: Donor): Observable<Donor> {
    const updateUrl = `${this.url}/${donor.id}`;
    return this.http.put<Donor>(updateUrl, donor);
  }

  deleteDonor(id: number): Observable<void> {
    const deleteUrl = `${this.url}/${id}`;
    return this.http.delete<void>(deleteUrl).pipe(
      tap(() => {
        const updatedDonorList = this.donorList$.getValue().filter(donor => donor.id !== id);
        this.donorList$.next(updatedDonorList);
      })
    );
  }

  getDonorById(donorId: number): Observable<Donor> {
    const url = `${this.url}/${donorId}`;
    return this.http.get<Donor>(url);
  }
}
