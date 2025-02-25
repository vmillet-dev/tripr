import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { AuthenticationService } from 'app/security/authentication.service';
import { AuthenticationRequest } from 'app/security/authentication.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-login',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './authentication.component.html'
})
export class AuthenticationComponent {

  authenticationService = inject(AuthenticationService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  loginForm = new FormGroup({
    email: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    password: new FormControl(null, [Validators.required, Validators.maxLength(72)]),
    rememberMe: new FormControl(false)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      loginError: $localize`:@@authentication.login.error:Your login was not successful - please try again.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.loginForm.markAllAsTouched();
    if (!this.loginForm.valid) {
      return;
    }
    const data = new AuthenticationRequest(this.loginForm.value);
    this.authenticationService.login(data)
        .subscribe({
          next: () => this.router.navigate([this.authenticationService.getLoginSuccessUrl()]),
          error: (error) => {
            if (error.status === 401) {
              this.loginForm.reset();
              this.router.navigate(['/login'], {
                state: {
                  msgError: this.getMessage('loginError')
                }
              });
            } else {
              this.errorHandler.handleServerError(error.error, this.loginForm, this.getMessage);
            }
          }
        });
  }

}
