import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { take } from 'rxjs';
import { AuthService } from "../services/auth.service";


export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  authService.currentUser$.pipe(take(1)).subscribe({
    next: (response) => {
      if (response) {
        req = req.clone({
          setHeaders: { Authorization: `Bearer ${response.accessToken}` },
        });
      }
    },
  });

  return next(req);
};
