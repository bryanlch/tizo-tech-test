import { Injectable, Inject, PLATFORM_ID, DOCUMENT } from '@angular/core';

import { Title } from '@angular/platform-browser';
import { environment } from '@env/environment';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
   providedIn: 'root'
})
export class StartupService {
   private readonly defaultTitle = environment.appTitle;
   private readonly gtmId = (environment as any).gtmId;

   constructor(
      @Inject(DOCUMENT) private document: Document,
      @Inject(PLATFORM_ID) private platformId: Object,
      private titleService: Title
   ) { }

   load(): Promise<void> {
      return new Promise((resolve) => {
         this.titleService.setTitle(this.defaultTitle);

         if (isPlatformBrowser(this.platformId)) {
            this.injectGtm();
         }

         resolve();
      });
   }

   private injectGtm(): void {
      if (!this.gtmId) {
         return;
      }
      const window = this.document.defaultView as any;

      if (this.document.getElementById('gtm-script') || !window) {
         return;
      }

      // GTM Script
      const script = this.document.createElement('script');
      script.id = 'gtm-script';
      script.async = true;
      script.src = `https://www.googletagmanager.com/gtm.js?id=${this.gtmId}`;

      // Inicialización del DataLayer
      window.dataLayer = window.dataLayer || [];
      window.dataLayer.push({
         'gtm.start': new Date().getTime(),
         event: 'gtm.js'
      });

      this.document.head.insertBefore(script, this.document.head.firstChild);

      // GTM NoScript (Opcional pero recomendado)
      const noscript = this.document.createElement('noscript');
      const iframe = this.document.createElement('iframe');
      iframe.src = `https://www.googletagmanager.com/ns.html?id=${this.gtmId}`;
      iframe.height = '0';
      iframe.width = '0';
      iframe.style.display = 'none';
      iframe.style.visibility = 'hidden';

      noscript.appendChild(iframe);
      this.document.body.insertBefore(noscript, this.document.body.firstChild);
   }
}
