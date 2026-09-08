import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { ApplicationConfig, LOCALE_ID, inject } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {
  NavigationError,
  Router,
  RouterFeatures,
  TitleStrategy,
  provideRouter,
  withComponentInputBinding,
  withDebugTracing,
  withNavigationErrorHandler,
} from '@angular/router';
import { provideServiceWorker } from '@angular/service-worker';

import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap/datepicker';
import Aura from '@primeuix/themes/aura';
import { environment } from 'environments/environment';
import { providePrimeNG } from 'primeng/config';

import { authExpiredInterceptor } from 'app/core/interceptor/auth-expired.interceptor';
import { authInterceptor } from 'app/core/interceptor/auth.interceptor';
import { errorHandlerInterceptor } from 'app/core/interceptor/error-handler.interceptor';
import { notificationInterceptor } from 'app/core/interceptor/notification.interceptor';

import './config/dayjs';

import { AppPageTitleStrategy } from './app-page-title-strategy';
import routes from './app.routes';
import { NgbDateDayjsAdapter } from './config/datepicker-adapter';

const routerFeatures: RouterFeatures[] = [
  withComponentInputBinding(),
  withNavigationErrorHandler((e: NavigationError) => {
    const router = inject(Router);
    if (e.error.status === 403) {
      router.navigate(['/accessdenied']);
    } else if (e.error.status === 404) {
      router.navigate(['/404']);
    } else if (e.error.status === 401) {
      router.navigate(['/login']);
    } else {
      router.navigate(['/error']);
    }
  }),
];
if (environment.DEBUG_INFO_ENABLED) {
  routerFeatures.push(withDebugTracing());
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideAnimationsAsync(),
    providePrimeNG({
      theme: { preset: Aura },
      license:
        'eyJpZCI6IjFjNjFjMDUwLTkxZjYtNDYyYy04NWVmLTg5MzQ0MDAxYmM1ZiIsInByb2R1Y3QiOiJwcmltZXVpIiwidGllciI6ImNvbW11bml0eSIsInR5cGUiOiJkZXYiLCJpYXQiOjE3ODg4NzEzNjMsImV4cCI6MTgyMDQwNzM2M30.dzXns1FMF6pt-CqDurP2bzi-CO7tmleFQnmihYSdLgzpC0x2-znBDJ7y4-DKCpIp8zdXuS_pZ_uZGVNRJrTwAQ',
    }),
    provideRouter(routes, ...routerFeatures),
    // Set this to true to enable service worker (PWA)
    provideServiceWorker('ngsw-worker.js', { enabled: false }),
    provideHttpClient(withInterceptors([authInterceptor, authExpiredInterceptor, errorHandlerInterceptor, notificationInterceptor])),
    Title,
    { provide: LOCALE_ID, useValue: 'es' },
    { provide: NgbDateAdapter, useClass: NgbDateDayjsAdapter },
    { provide: TitleStrategy, useClass: AppPageTitleStrategy },
  ],
};
