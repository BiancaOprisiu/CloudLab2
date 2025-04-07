import {Directive, ElementRef, Input, OnInit} from '@angular/core';

@Directive({
  selector: '[appDonationReporting]'
})
export class DonationReportingDirective implements OnInit{
  @Input() perms: string[];

  constructor(private elr:ElementRef) {
  }

  ngOnInit() {
    if (this.perms && this.perms.find(perm => perm === "DONATION_REPORTING")==null) {
      this.elr.nativeElement.style.display = 'none';
    }
    else {
      this.elr.nativeElement.style.display = 'show';
    }
  }
}
