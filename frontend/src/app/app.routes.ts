import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './auth/login/login.component';
import { PasswordResetStartComponent } from './auth/pwd-reset-start/password-reset.start.component';
import { PasswordResetCompleteComponent } from './auth/pwd-reset-complete/password-reset.complete.component';
import { RegisterComponent } from './auth/register/register.component';
import { ErrorComponent } from './common/error/error.component';
import {AuthService} from "./shared/services/auth.service";


export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: $localize`:@@home.index.headline:Welcome to your new app!`
  },
  {
    path: 'login',
    component: LoginComponent,
    title: $localize`:@@authentication.login.headline:Login`
  },
  {
    path: 'passwordReset/start',
    component: PasswordResetStartComponent,
    title: $localize`:@@passwordReset.start.headline:Start password reset`
  },
  {
    path: 'passwordReset/complete',
    component: PasswordResetCompleteComponent,
    title: $localize`:@@passwordReset.complete.headline:Complete password reset`
  },
  {
    path: 'register',
    component: RegisterComponent,
    title: $localize`:@@registration.register.headline:Registration`
  },
  {
    path: 'error',
    component: ErrorComponent,
    title: $localize`:@@error.headline:Error`
  },
  {
    path: '**',
    component: ErrorComponent,
    title: $localize`:@@notFound.headline:Page not found`
  }
];

// add authentication check to all routes
for (const route of routes) {
  route.canActivate = [(route: ActivatedRouteSnapshot) => inject(AuthService).checkAccessAllowed(route)];
}
