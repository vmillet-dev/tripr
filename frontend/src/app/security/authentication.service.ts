import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { tap } from 'rxjs';
import { AuthenticationRequest, AuthenticationResponse, RegistrationRequest } from './authentication.model';
import {environment} from "../../environments/environment";


export const ADMIN = 'ADMIN';
export const MEMBER = 'MEMBER';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {

  http = inject(HttpClient);
  router = inject(Router);
  loginPath = environment.apiPath + '/authenticate';
  refreshPath = environment.apiPath + '/refresh';
  registerPath = environment.apiPath + '/register';

  loginSuccessUrl: string = '/';
  updateIntervalSeconds: number = 15;

  init() {
    // check refresh token on application start and every x seconds
    this.updateRefreshToken();
    setInterval(() => this.updateRefreshToken(), this.updateIntervalSeconds * 1000);
  }

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      loginRequired: $localize`:@@authentication.login.required:Please login to access this area.`,
      missingRole: $localize`:@@authentication.role.missing:You do not have sufficient rights to access this area.`,
      logoutSuccess: $localize`:@@authentication.logout.success:Your logout was successful.`
    };
    return messages[key];
  }

  checkAccessAllowed(route: ActivatedRouteSnapshot) {
    const roles = route.data['roles'];
    const targetUrl = this.router.getCurrentNavigation()?.finalUrl?.toString();
    if (targetUrl && !this.isLoggedIn() && ['/login', '/register', '/error'].indexOf(targetUrl) === -1) {
      this.loginSuccessUrl = targetUrl;
    }
    if (roles && !this.isLoggedIn()) {
      // show login page
      this.router.navigate(['/login'], {
            state: {
              msgInfo: this.getMessage('loginRequired')
            }
          });
      return false;
    } else if (roles && !this.hasAnyRole(roles)) {
      // show error page with message
      this.router.navigate(['/error'], {
            state: {
              errorStatus: '403',
              msgError: this.getMessage('missingRole')
            }
          });
      return false;
    }
    return true;
  }

  getLoginSuccessUrl() {
    return this.loginSuccessUrl;
  }

  login(authenticationRequest: AuthenticationRequest) {
    return this.http.post<AuthenticationResponse>(this.loginPath, authenticationRequest)
        .pipe(tap((data) => this.setSession(data)));
  }

  setSession(authenticationResponse: AuthenticationResponse) {
    localStorage.setItem('access_token', authenticationResponse.accessToken!);
    localStorage.setItem('refresh_token', authenticationResponse.refreshToken!);
  }

  isLoggedIn() {
    // check token available
    if (!this.getToken()) {
      return false;
    }
    // check token not expired
    return this.getCurrentSeconds() < this.getTokenData().exp;
  }

  updateRefreshToken() {
    const refreshToken = localStorage.getItem('refresh_token');
    if (!refreshToken) {
      return;
    }
    if (this.getToken() && this.getCurrentSeconds() + this.updateIntervalSeconds < this.getTokenData().exp) {
      // token not expired yet
      return;
    }
    this.http.post<AuthenticationResponse>(this.refreshPath, { refreshToken })
        .subscribe({
          next: (data) => this.setSession(data),
          error: (error) => {
            if (error.status === 401) {
              // next action will trigger the login screen
              localStorage.removeItem('access_token');
              localStorage.removeItem('refresh_token');
            }
          }
        });
  }

  hasAnyRole(requiredRoles: string[]) {
    const tokenData = this.getTokenData();
    return requiredRoles.some((requiredRole) => tokenData.roles.includes(requiredRole));
  }

  getToken() {
    return localStorage.getItem('access_token');
  }

  getTokenData() {
    const token = this.getToken()!!;
    return JSON.parse(atob(token.split('.')[1]));
  }

  getCurrentSeconds() {
    return Math.floor((new Date()).getTime() / 1000);
  }

  logout() {
    if (this.isLoggedIn()) {
      this.loginSuccessUrl = '/';
    }
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    this.router.navigate(['/login'], {
          state: {
            msgInfo: this.getMessage('logoutSuccess')
          }
        });
  }

  register(registrationRequest: RegistrationRequest) {
    return this.http.post(this.registerPath, registrationRequest);
  }

}
