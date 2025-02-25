import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AuthenticationComponent } from './security/authentication.component';
import { PasswordResetStartComponent } from './security/passwordReset.start.component';
import { PasswordResetCompleteComponent } from './security/passwordReset.complete.component';
import { RegistrationComponent } from './security/registration.component';
import { ErrorComponent } from './error/error.component';
import { AuthenticationService } from 'app/security/authentication.service';


export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: $localize`:@@home.index.headline:Welcome to your new app!`
  },
  {
    path: 'login',
    component: AuthenticationComponent,
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
    component: RegistrationComponent,
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
  route.canActivate = [(route: ActivatedRouteSnapshot) => inject(AuthenticationService).checkAccessAllowed(route)];
}
