import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { StorageService } from '../../services/storage.service';

export const authGuard: CanActivateFn = (route, state) => {
  const storageService = inject(StorageService);
  const router = inject(Router);

  if (!storageService.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  const expectedPermissions: string[] = route.data?.['roles'] || [];
  if (expectedPermissions.length === 0) {
    return true;
  }

  const permissoesUsuario = storageService.getPermissoes();
  const hasAccess = expectedPermissions.some(role => permissoesUsuario.includes(role));

  if (!hasAccess) {
    router.navigate(['/login']);
    return false;
  }
  return true;
};
