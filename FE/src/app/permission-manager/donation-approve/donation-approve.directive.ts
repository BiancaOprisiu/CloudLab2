import {Directive, ElementRef, Input, OnInit} from '@angular/core';

@Directive({
  selector: '[appDonationApprove]'
})
export class DonationApproveDirective implements OnInit{
  @Input() perms: string[];

  constructor(private elr:ElementRef) {
  }

  ngOnInit() {
    if (this.perms && this.perms.find(perm => perm === "DONATION_APPROVE")==null) {
      this.elr.nativeElement.style.display = 'none';
    }
    else {
      this.elr.nativeElement.style.display = 'show';
    }
  }
}
