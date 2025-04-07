import {Directive, ElementRef, Input, OnInit} from '@angular/core';

@Directive({
  selector: '[appUserManagement]'
})
export class UserManagementDirective implements OnInit{
  @Input() perms: string[];

  constructor(private elr:ElementRef) {
  }

  ngOnInit() {
    if (this.perms && this.perms.find(perm => perm === "USER_MANAGEMENT")==null) {
      this.elr.nativeElement.style.display = 'none';
    }
    else {
      this.elr.nativeElement.style.display = 'show';
    }
  }

}
