import {
  EnvironmentProviders,
  inject,
  makeEnvironmentProviders,
  provideAppInitializer,
} from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { StartupService } from './services/startup.service';
import { AuthInterceptor } from './interceptors/auth.interceptor';

export function providerCore(): EnvironmentProviders {
  return makeEnvironmentProviders([
    provideHttpClient(withInterceptors([AuthInterceptor])),

    provideAppInitializer(() => {
      const startupService = inject(StartupService);
      return startupService.load();
    }),
  ]);
}
