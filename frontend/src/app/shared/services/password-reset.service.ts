import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {environment} from "../../../environments/environment";
import {PasswordResetCompleteRequest, PasswordResetRequest} from "../../auth/password-reset.model";
import {ApiService} from "./api.service";


@Injectable({
  providedIn: 'root',
})
export class PasswordResetService {

  private apiService: ApiService = inject(ApiService);
  resourcePath = '/passwordReset';

  public start(passwordResetRequest: PasswordResetRequest) {
    return this.apiService.post<void>(this.resourcePath + '/start', passwordResetRequest);
  }

  public isValidUid(passwordResetUid: string) {
    return this.apiService.get<boolean>(this.resourcePath + '/isValidUid', { params: { uid: passwordResetUid } });
  }

  public complete(passwordResetCompleteRequest: PasswordResetCompleteRequest) {
    return this.apiService.post<void>(this.resourcePath + '/complete', passwordResetCompleteRequest);
  }

}
