import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from '../../common/input-row/input-row.component';
import { AuthService } from '../../shared/services/auth.service';
import { RegistrationRequest } from '../auth.model';
import { ErrorHandler } from '../../common/error-handler.injectable';
import {TranslocoPipe} from "@jsverse/transloco";


@Component({
  selector: 'app-registration',
  imports: [CommonModule, ReactiveFormsModule, InputRowComponent, TranslocoPipe],
  templateUrl: './register.component.html'
})
export class RegisterComponent {

  authenticationService = inject(AuthService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  registrationForm = new FormGroup({
    email: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    password: new FormControl(null, [Validators.required, Validators.maxLength(72)])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      registrationSuccess: $localize`:@@registration.register.success:Your registration is complete. You may now login.`,
      REGISTRATION_REQUEST_EMAIL_UNIQUE: $localize`:@@registration.register.taken:This account is already taken. Please login.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.registrationForm.markAllAsTouched();
    if (!this.registrationForm.valid) {
      return;
    }
    const data = new RegistrationRequest(this.registrationForm.value);
    this.authenticationService.register(data)
        .subscribe({
          next: () => this.router.navigate(['/login'], {
            state: {
              msgSuccess: this.getMessage('registrationSuccess')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.registrationForm, this.getMessage)
        });
  }

}
