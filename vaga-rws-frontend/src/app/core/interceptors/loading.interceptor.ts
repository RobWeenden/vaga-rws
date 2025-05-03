import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize } from 'rxjs';
import { LoadingService } from '../../services/loading.service';

export const loadingrInterceptor: HttpInterceptorFn = (req, next) => {
  const loadingService = inject(LoadingService);

  if (loadingService.isExcluded(req.url)) {
    return next(req);
  }
  loadingService.show();

  return next(req)
    .pipe(finalize(() => loadingService.hide()));
};
