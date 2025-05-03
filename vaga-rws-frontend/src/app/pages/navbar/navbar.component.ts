import { CommonModule } from "@angular/common";
import { Component, EventEmitter, Output } from "@angular/core";
import { Router, RouterModule } from "@angular/router";
import { MaterialModule } from '../../../shared/material.module';
import { PermissionsDirective } from '../../core/directives/permissions.directive';
import { AuthService } from '../../services/auth.service';
import { StorageService } from '../../services/storage.service';


@Component({
  selector: 'app-navbar',
  imports: [
    CommonModule,
    RouterModule,
    MaterialModule,
    PermissionsDirective
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
})
export class NavbarComponent {
  @Output() toggleSidebar = new EventEmitter<void>();
  @Output() themeToggle = new EventEmitter<void>();
  isDarkTheme = false;

  constructor(
    private readonly router: Router,
    private readonly authService: AuthService,
    private readonly storageService: StorageService
  ) { }

  open(url:string) {
    this.router.navigate([`/${url}`]);
  }

  logout() {
    this.authService.logout();
  }

  hasPermissionAdmin(): string {
    return this.storageService.getPermissaoAdmin();
  }


}
