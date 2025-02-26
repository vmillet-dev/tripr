import { HTTP_INTERCEPTORS, provideHttpClient } from '@angular/common/http';
import { APP_INITIALIZER, ApplicationConfig, provideZoneChangeDetection, isDevMode } from '@angular/core';
import { ExtraOptions, TitleStrategy, provideRouter} from '@angular/router';
import { TranslocoHttpLoader } from './transloco-loader';
import { provideTransloco } from '@jsverse/transloco';
import { AuthenticationInterceptor } from './security/authentication.injectable';
import {routes} from "./app.routes";
import {AuthenticationService} from "./security/authentication.service";
import {CustomTitleStrategy} from "./common/title-strategy.injectable";


const routeConfig: ExtraOptions = {
  onSameUrlNavigation: 'reload',
  scrollPositionRestoration: 'enabled'
};

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    {
      provide: TitleStrategy,
      useClass: CustomTitleStrategy
    },
    {
      provide: APP_INITIALIZER,
      useFactory:  (authenticationService: AuthenticationService) => {
        return () => authenticationService.init();
      },
      multi: true,
      deps: [AuthenticationService]
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthenticationInterceptor,
      multi: true
    },
    provideRouter(routes),
    provideHttpClient(),
    provideTransloco({
        config: {
          availableLangs: ['en'],
          defaultLang: 'en',
          // Remove this option if your application doesn't support changing language in runtime.
          reRenderOnLangChange: true,
          prodMode: !isDevMode(),
        },
        loader: TranslocoHttpLoader
      })
  ]
};
