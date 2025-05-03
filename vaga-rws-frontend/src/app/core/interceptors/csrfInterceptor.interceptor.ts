import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';

export const csrfInterceptor: HttpInterceptorFn = (req, next) => {
  const cookieService = inject(CookieService);
  const csrfToken = cookieService.get('XSRF-TOKEN');

  let cloneRequest = req.clone({
    withCredentials: true
  });

  if (csrfToken && !req.url.includes('/auth/login')) {
    cloneRequest = cloneRequest.clone({
      headers: cloneRequest.headers.set('X-XSRF-TOKEN', csrfToken)
    });
  }

  return next(cloneRequest);
};
