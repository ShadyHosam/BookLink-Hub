import { Component } from '@angular/core';
import {AuthenticateRequest} from "../services/models/authenticate-request";
import {Router} from "@angular/router";
import {AuthenticationService} from "../services/services/authentication.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  authRequest: AuthenticateRequest = {email:'' , password:''};
  errorMsg: Array<string> = [];

  constructor(
    private router:Router,
    private authService:AuthenticationService,
    // anoter service ineed
    ) {

  }


  login() {
  this.errorMsg = [];

  this.authService.authenticate({body:this.authRequest}).
  subscribe(
    {
      next:(res) =>{
        //redirect to the books page
        this.router.navigate(['books']);
      },
     error:(err) =>{
        console.log(err);
      if(err.error.validationErrors){
        this.errorMsg = err.error.validationErrors;
      }
      else{
        this.errorMsg.push(err.error.error);
      }

     }
    }
  )
  }



  register() {
  this.router.navigate(['register'])
  }
}
