import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { PasswordResetService } from 'app/security/passwordReset.service';
import { PasswordResetRequest } from 'app/security/passwordReset.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-passwordReset',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, InputRowComponent],
  templateUrl: './passwordReset.start.component.html'
})
export class PasswordResetStartComponent {

  passwordResetService = inject(PasswordResetService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  passwordResetStartForm = new FormGroup({
    email: new FormControl(null, [Validators.required, Validators.maxLength(255)])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      resetStarted: $localize`:@@passwordReset.started:You will receive an e-mail to reset your password in just a few moments.`,
      PASSWORD_RESET_REQUEST_EMAIL_EXISTS: $localize`:@@passwordReset.start.noAccount:No account could be found for the given e-mail.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.passwordResetStartForm.markAllAsTouched();
    if (!this.passwordResetStartForm.valid) {
      return;
    }
    const data = new PasswordResetRequest(this.passwordResetStartForm.value);
    this.passwordResetService.start(data)
        .subscribe({
          next: () => this.router.navigate(['/login'], {
            state: {
              msgInfo: this.getMessage('resetStarted')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.passwordResetStartForm, this.getMessage)
        });
  }

}
