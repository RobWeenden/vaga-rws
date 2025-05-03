import { HttpInterceptorFn } from '@angular/common/http';

export const authXhrInterceptor: HttpInterceptorFn = (req, next) => {
  const cloneRequest = req.clone({
    withCredentials: true
  });
  return next(cloneRequest);
};
