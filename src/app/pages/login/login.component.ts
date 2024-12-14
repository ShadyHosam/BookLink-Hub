import { Component } from '@angular/core';
import {AuthenticateRequest} from "../../services/models/authenticate-request";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import {TokenService} from "../../services/token/token.service";

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
    private tokenService: TokenService)
  {

  }



  login() {
  this.errorMsg = [];
  this.authService.authenticate({body:this.authRequest}).
  subscribe(
    {
      next:(res) =>{

        this.tokenService.token=  res.token as string;
        //redirect to the books page
        //this.router.navigate(['books']);
        console.log(this.tokenService.token);
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
