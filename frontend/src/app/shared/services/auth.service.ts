import { Injectable, inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import {BehaviorSubject, Observable, tap } from 'rxjs';
import { AuthenticationRequest, AuthenticationResponse, RegistrationRequest } from '../../auth/auth.model';
import { LocalStorageService } from "./local-storage.service";
import { ApiService } from "./api.service";


export const ADMIN = 'ADMIN';
export const MEMBER = 'MEMBER';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private router: Router = inject(Router);
  private apiService: ApiService = inject(ApiService);
  private localStorageService: LocalStorageService = inject(LocalStorageService);

  loginSuccessUrl: string = '/';
  updateIntervalSeconds: number = 15;

  private currentUserSubject: BehaviorSubject<AuthenticationResponse | null>;
  public currentUser$: Observable<AuthenticationResponse | null>;

  constructor() {
    this.currentUserSubject = new BehaviorSubject<AuthenticationResponse | null>(this.getUserFromStorage());
    this.currentUser$ = this.currentUserSubject.asObservable();

    this.updateRefreshToken();
    setInterval(() => this.updateRefreshToken(), this.updateIntervalSeconds * 1000);
  }

  private getUserFromStorage(): AuthenticationResponse | null {
    const storedUser = this.localStorageService.getData('currentUser');
    return storedUser ? JSON.parse(storedUser) : null;
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
    return this.apiService.post<AuthenticationResponse>('/authenticate', authenticationRequest)
        .pipe(tap((data) => this.setSession(data)));
  }

  setSession(authenticationResponse: AuthenticationResponse) {
    this.localStorageService.saveData('access_token', authenticationResponse.accessToken!);
    this.localStorageService.saveData('refresh_token', authenticationResponse.refreshToken!);
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
    const refreshToken = this.localStorageService.getData('refresh_token');
    if (!refreshToken) {
      return;
    }
    if (this.getToken() && this.getCurrentSeconds() + this.updateIntervalSeconds < this.getTokenData().exp) {
      // token not expired yet
      return;
    }
    this.apiService.post<AuthenticationResponse>('/refresh', { refreshToken })
        .subscribe({
          next: (data) => this.setSession(data),
          error: (error) => {
            if (error.status === 401) {
              // next action will trigger the login screen
              this.localStorageService.removeData('access_token');
              this.localStorageService.removeData('refresh_token');
            }
          }
        });
  }

  hasAnyRole(requiredRoles: string[]) {
    const tokenData = this.getTokenData();
    return requiredRoles.some((requiredRole) => tokenData.roles.includes(requiredRole));
  }

  getToken() {
    return this.localStorageService.getData('access_token');
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
    this.localStorageService.removeData('access_token');
    this.localStorageService.removeData('refresh_token');
    this.router.navigate(['/login'], { state: { msgInfo: this.getMessage('logoutSuccess') } }).then();
  }

  register(registrationRequest: RegistrationRequest) {
    return this.apiService.post<void>('/register', registrationRequest);
  }

}
