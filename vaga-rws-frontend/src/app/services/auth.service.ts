import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of, switchMap, take } from 'rxjs';
import { environment } from '../../environments/environments';
import { LoginRequest } from '../core/models/login-request.component';
import { StorageService } from './storage.service';


const API_URL = environment.backend;
const AUTH_URL = API_URL + '/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  isLoggedIn = false;
  isLoginFailed = false;
  roles: string[] = [];

  constructor(
    private readonly http: HttpClient,
    private readonly storageService: StorageService,
    private readonly router: Router
  ) { }

  login(loginRequest: LoginRequest): Observable<any> {
    return this.http.post<{ token: string }>(AUTH_URL + '/login', loginRequest)
      .pipe(
        switchMap((response) => {
          this.storageService.addUsuarioStorage(response);
          this.storageService.setAuthenticated(true);
          return of(response);
        })
      );
  }

  reloadPage(): void {
    window.location.reload();
  }

  logout() {
    this.storageService.removerUserLoggado();
    this.router.navigate(['/logout']);
    return this.http.post(AUTH_URL + '/logout', {}).pipe(take(1));
  }

}
