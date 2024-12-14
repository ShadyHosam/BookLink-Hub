import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import {error} from "@angular/compiler-cli/src/transformers/util";

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.scss'
})
export class ActivateAccountComponent {
  message: string = '';
  isOkay = true;
  submitted = false;

  constructor(
    private router: Router,
    private authService: AuthenticationService,
  ) {
  }

  onCodeCompleted(token: string) {
    this.confirmAccount(token);
  }

  redirectToLogin() {
    this.router.navigate(['/login']);
  }

  // we can use the auth service here to confirm the account
  private confirmAccount(token: string) {

    this.authService.confirm({token}).subscribe({
      next: () => {
        this.message = 'Your account has been activated successfully.\nNow you can login';
        this.submitted = true;
        this.isOkay=true;
      },
      error: () => {
        this.message = 'Token has been expired or invalid';
        this.submitted = true;
        this.isOkay = false;
      }
    })
  }
}





