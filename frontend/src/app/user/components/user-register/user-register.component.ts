import {ChangeDetectorRef, Component, Inject, OnInit} from '@angular/core';
import {Router, Routes} from "@angular/router";
import { AbstractControl, FormBuilder, FormControl, FormControlName, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {User} from "../../models/user";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Role} from "../../../roles/models/role";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {CampaignService} from "../../../campaigns/services/campaign.service";
import {switchMap, tap} from "rxjs";
import {Campaign} from "../../../campaigns/models/campaign";
import {query} from "@angular/animations";



@Component({
  selector: 'app-user-register',
  templateUrl: './user-register.component.html',
  styleUrls: ['./user-register.component.css']
})
export class UserRegisterComponent implements OnInit {

  registerForm:FormGroup;
  roleList:String[]=["ROLE_ADM", "ROLE_MGN", "ROLE_CEN", "ROLE_REP"];
  repSelected:boolean=false;
  campaignList$:any;
  campaignSelected:boolean=false;
  constructor(public dialogRef: MatDialogRef<UserRegisterComponent>,
              @Inject(MAT_DIALOG_DATA) public data: User, private fb: FormBuilder, private userService: UserService,
              private campaignService:CampaignService, private router: Router, private _snackBar: MatSnackBar,
              private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.campaignService.loadCampaigns().pipe(
      switchMap(()=>this.campaignService.getCampaigns()),
      tap(camps=>this.campaignList$ = camps)
    ).subscribe();

    this.registerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.email, Validators.required]],
      phoneNumber: ['', Validators.pattern(/^(?:(?:\+?40|0040)|0)?7\d{8}$/)],
      roles: ['', Validators.required],
      campaigns:['',this.conditionalRequiredValidator(false)]});

      this.registerForm.controls['roles'].valueChanges.subscribe(value => {
        this.repSelected = false;
        this.registerForm.get('roles')?.value.forEach((role:string) => {
          if(role === 'ROLE_REP'){
            this.repSelected = true;
          }
        })
        this.updateInput(this.repSelected)
      })
  }
  onSave(userToAdd: User){
    userToAdd.firstname=this.registerForm.get('firstName')?.value;
    userToAdd.lastname=this.registerForm.get('lastName')?.value;
    userToAdd.email=this.registerForm.get('email')?.value;
    userToAdd.mobilenumber=this.registerForm.get('phoneNumber')?.value;
    let newRoleList:Role[]=[];
    this.registerForm.get('roles')?.value.forEach((role:string)=> {
      let newRole = new Role(this.roleList.indexOf(role)+1,role, []);
      newRoleList.push(newRole);
    });
    userToAdd.roles = newRoleList;
    if(this.repSelected){
      let newCampList:Campaign[]=[];
      this.registerForm.get('campaigns')?.value.forEach((camp:Campaign) => {
        newCampList.push(camp)
      });
      userToAdd.campaigns = newCampList;
    }
    this.userService.addUser(userToAdd).subscribe(()=>{
      this._snackBar.open("Successful registrations", "Ok", {
        duration: 3000,
        panelClass: 'ok-snack'
      });
      this.userService.loadUsers().subscribe(()=>this.dialogRef.close());
    },
      (error)=>{
        this._snackBar.open(error.message, "Ok", {
          duration: 3000,
          panelClass: 'error-snack'
        });
      });
  }

  updateInput(value:boolean){
    const dependentControl = this.registerForm.get('campaigns');
    if(value){
      dependentControl?.setValidators(this.conditionalRequiredValidator(true));
    }
    else{
      dependentControl?.setValidators(this.conditionalRequiredValidator(false));
    }
    dependentControl?.updateValueAndValidity();
  }

  conditionalRequiredValidator(condition: boolean): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      if (condition && (!control.value || control.value.length == 0)) {
        return { required: true };
      }
      return null;
    };
  }

  checkCamp(){
    this.campaignSelected = false;
    let campList:Campaign[] = this.registerForm.get('campaigns')?.value;
    this.campaignSelected = campList.length != 0;
  }
}
