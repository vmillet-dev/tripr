import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {environment} from "../../../environments/environment";
import {PasswordResetCompleteRequest, PasswordResetRequest} from "../../auth/password-reset.model";


@Injectable({
  providedIn: 'root',
})
export class PasswordResetService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/passwordReset';

  public start(passwordResetRequest: PasswordResetRequest) {
    return this.http.post<void>(this.resourcePath + '/start', passwordResetRequest);
  }

  public isValidUid(passwordResetUid: string) {
    return this.http.get<boolean>(this.resourcePath + '/isValidUid', { params: { uid: passwordResetUid } });
  }

  public complete(passwordResetCompleteRequest: PasswordResetCompleteRequest) {
    return this.http.post<void>(this.resourcePath + '/complete', passwordResetCompleteRequest);
  }

}
