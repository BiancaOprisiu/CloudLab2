import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {User} from "../../models/user";
import {AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Role} from "../../../roles/models/role";
import {CampaignService} from "../../../campaigns/services/campaign.service";
import {delay, switchMap, tap} from "rxjs";
import {Campaign} from "../../../campaigns/models/campaign";
import {Router} from "@angular/router";

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit {
  campaignSelected: boolean = false;

  constructor(public dialogRef: MatDialogRef<UserEditComponent>,
              @Inject(MAT_DIALOG_DATA) public data: User, private userService: UserService, private campaignService:CampaignService,
              private fb: FormBuilder, private _snackBar: MatSnackBar, private router: Router) { }

  registerForm:FormGroup;

  roleList:String[]=["ROLE_ADM", "ROLE_MGN", "ROLE_CEN", "ROLE_REP"];
  repSelected:boolean=false;
  campaignList$:any;

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.campaignService.loadCampaigns().pipe(
      switchMap(()=>this.campaignService.getCampaigns()),
      tap(camps=>this.campaignList$ = camps)
    ).subscribe();
    const initialRoleNames = this.data.roles.map(role => role.name);
    const initialCampaigns = this.data.campaigns.map(camp => camp.name);
    for(let role of this.data.roles){
      if(role.name == "ROLE_REP"){
        this.repSelected = true
      }
    }

    this.registerForm = this.fb.group({
      firstName: [this.data.firstname, Validators.required],
      lastName: [this.data.lastname, Validators.required],
      email: [this.data.email, Validators.email],
      phoneNumber: [this.data.mobilenumber, Validators.pattern(/^(?:(?:\+?40|0040)|0)?7\d{8}$/)],
      roles: [initialRoleNames, Validators.required],
      campaigns:[this.data.campaigns,this.conditionalRequiredValidator(this.repSelected)]
    });
    this.checkRoleRep();
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

  onSave(userToUpdate: User){
    userToUpdate.firstname=this.registerForm.get('firstName')?.value;
    userToUpdate.lastname=this.registerForm.get('lastName')?.value;
    userToUpdate.email=this.registerForm.get('email')?.value;
    userToUpdate.mobilenumber=this.registerForm.get('phoneNumber')?.value;
    let newRoleList:Role[]=[];
    this.registerForm.get('roles')?.value.forEach((role:string)=> {
      let newRole = new Role(this.roleList.indexOf(role)+1,role, []);
      newRoleList.push(newRole);
    });
    if(this.repSelected){
      let newCampList:Campaign[]=[];
      this.registerForm.get('campaigns')?.value.forEach((camp:Campaign) => {
        newCampList.push(camp)
      });
      userToUpdate.campaigns = newCampList;
    }
    //@ts-ignore
    let id = JSON.parse(localStorage.getItem("user")).id;
    userToUpdate.roles = newRoleList;
    userToUpdate.id=this.data.id;
    userToUpdate.username=this.data.username;
    userToUpdate.password=this.data.password;
    this.userService.updateUser(userToUpdate).subscribe(() => {
      if(userToUpdate.id == id){
        this._snackBar.open("Successfully edited, you will be logged off", "Ok", {
          duration: 2000,
          panelClass: 'ok-snack'
        });
        delay(20000);
        localStorage.clear();
        this.router.navigate(["/login"])
      }
      else{
          this._snackBar.open("Successfully edited", "Ok", {
            duration: 3000,
            panelClass: 'ok-snack'
          });
          this.initForm();
          this.userService.loadUsers().subscribe();
        }
      },
      (error)=>{
        this._snackBar.open(error.message(), "Ok", {
          duration: 3000,
          panelClass: 'error-snack'
        });
        this.initForm();
      });
    this.dialogRef.close();
  }
  checkRoleRep(){
    this.repSelected = false;
    let newRoleList:Role[]=[];
    this.registerForm.get('roles')?.value.forEach((role:string)=> {
      let newRole = new Role(this.roleList.indexOf(role)+1,role, []);
      newRoleList.push(newRole);
    });
    for(let roles of newRoleList){
      if(roles.id == 4 && roles.name == "ROLE_REP"){
        this.repSelected = true;
      }
    }
  }

  checkCamp(){
    this.campaignSelected = false;
    let campList:Campaign[] = this.registerForm.get('campaigns')?.value;
    this.campaignSelected = campList.length != 0;
  }

  comparator(c1:Campaign, c2:Campaign):boolean{
    return c1.name == c2.name && c1.id == c2.id && c1.purpose == c2.purpose;
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

}
