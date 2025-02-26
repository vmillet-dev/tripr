import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { ApplicationConfig, provideZoneChangeDetection, isDevMode } from '@angular/core';
import { TitleStrategy, provideRouter } from '@angular/router';
import { TranslocoHttpLoader } from './transloco-loader';
import { provideTransloco } from '@jsverse/transloco';
import { authInterceptor } from './shared/interceptor/auth.interceptor';
import { routes } from "./app.routes";
import { CustomTitleStrategy } from "./common/title-strategy.injectable";

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    provideTransloco({
        config: {
          availableLangs: ['en'],
          defaultLang: 'en',
          // Remove this option if your application doesn't support changing language in runtime.
          reRenderOnLangChange: true,
          prodMode: !isDevMode(),
        },
        loader: TranslocoHttpLoader
      }),
    { provide: TitleStrategy, useClass: CustomTitleStrategy },
  ]
};
