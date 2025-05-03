import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

const USER_KEY = 'usuario';
const USER_AUTHENTICATED = 'isAuthenticated';

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  private readonly permissaoSubject = new BehaviorSubject<string[]>([]);
  permissoes$: Observable<string[]> = this.permissaoSubject.asObservable();

  constructor() { }

  clean(): void {
    window.sessionStorage.clear();
  }

  public addUsuarioStorage(user: any): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
    this.permissaoSubject.next(user.roles);
  }

  public usuarioLogado(): any {
    const user = window.sessionStorage.getItem(USER_KEY);
    if (user) {
      return JSON.parse(user);
    }
    return {};
  }

  public isLoggedIn(): boolean {
    const user = window.sessionStorage.getItem(USER_KEY);
    if (user) {
      return true;
    }

    return false;
  }

  removerUserLoggado() {
    sessionStorage.removeItem(USER_KEY);
    sessionStorage.removeItem(USER_AUTHENTICATED);
    this.permissaoSubject.next([]);
  }

  getAuthenticated(): string | null {
    return sessionStorage.getItem('isAuthenticated');
  }

  isAuthenticated(): boolean {
    return !!this.getAuthenticated();
  }

  setAuthenticated(value: boolean) {
    sessionStorage.setItem('isAuthenticated', `${value}`);
  }

  permissions(permissao: string): boolean {
    return this.permissaoSubject.value.includes(permissao);
  }

  getPermissoes(): string[] {
    const usarioLogado = this.usuarioLogado();
    if (!usarioLogado?.roles) {
      return [];
    }
    return Array.isArray(usarioLogado.roles) ? usarioLogado.roles : [];
  }

  getPermissaoAdmin(): string {
    const usarioLogado = this.usuarioLogado();
    const roles = usarioLogado?.roles;

    if (roles?.includes('ROLE_ADMIN')) {
      return usarioLogado?.roles;
    }
    return '';
  }

  possuiPermissionAdmin(): boolean {
    const usarioLogado = this.usuarioLogado();
    const roles = usarioLogado?.roles;

    if (roles?.includes('ROLE_ADMIN')) {
      return true;
    }
    return false;
  }

  possuiPermissionResponsavel(): boolean {
    const usarioLogado = this.usuarioLogado();
    const roles = usarioLogado?.roles;

    if (roles?.includes('ROLE_RESPONSAVEL')) {
      return true;
    }
    return false;
  }

}
