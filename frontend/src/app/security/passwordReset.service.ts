import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {environment} from "../../environments/environment";
import {PasswordResetCompleteRequest, PasswordResetRequest} from "./passwordReset.model";


@Injectable({
  providedIn: 'root',
})
export class PasswordResetService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/passwordReset';

  start(passwordResetRequest: PasswordResetRequest) {
    return this.http.post<void>(this.resourcePath + '/start', passwordResetRequest);
  }

  isValidUid(passwordResetUid: string) {
    return this.http.get<boolean>(this.resourcePath + '/isValidUid', { params: { uid: passwordResetUid } });
  }

  complete(passwordResetCompleteRequest: PasswordResetCompleteRequest) {
    return this.http.post<void>(this.resourcePath + '/complete', passwordResetCompleteRequest);
  }

}
