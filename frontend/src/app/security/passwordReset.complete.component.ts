import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from '../common/input-row/input-row.component';
import { PasswordResetService } from './passwordReset.service';
import { PasswordResetCompleteRequest } from './passwordReset.model';
import { ErrorHandler } from '../common/error-handler.injectable';
import {TranslocoPipe} from "@jsverse/transloco";


@Component({
  selector: 'app-passwordReset',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, InputRowComponent, TranslocoPipe],
  templateUrl: './passwordReset.complete.component.html'
})
export class PasswordResetCompleteComponent implements OnInit {

  passwordResetService = inject(PasswordResetService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);
  passwordResetUid = '';

  passwordResetCompleteForm = new FormGroup({
    newPassword: new FormControl(null, [Validators.required, Validators.maxLength(255)])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      resetInvalid: $localize`:@@passwordReset.invalid:The given link is invalid or expired. Please restart the process to set a new password.`,
      resetCompleted: $localize`:@@passwordReset.completed:Your password has been successfully changed. Please login.`,
      PASSWORD_RESET_REQUEST_EMAIL_EXISTS: $localize`:@@passwordReset.start.noAccount:No account could be found for the given e-mail.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.passwordResetUid = this.route.snapshot.queryParamMap.get('uid') || '';
    this.passwordResetService.isValidUid(this.passwordResetUid)
        .subscribe({
          next: (data) => {
            if (data !== true) {
              this.router.navigate(['/login'], {
                state: {
                  msgError: this.getMessage('resetInvalid')
                }
              });
            }
          },
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.passwordResetCompleteForm.markAllAsTouched();
    if (!this.passwordResetCompleteForm.valid) {
      return;
    }
    const data = new PasswordResetCompleteRequest(this.passwordResetCompleteForm.value);
    data.uid = this.passwordResetUid;
    this.passwordResetService.complete(data)
        .subscribe({
          next: () => this.router.navigate(['/login'], {
            state: {
              msgInfo: this.getMessage('resetCompleted')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.passwordResetCompleteForm, this.getMessage)
        });
  }

}
