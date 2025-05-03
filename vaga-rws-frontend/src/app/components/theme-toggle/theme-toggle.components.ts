import { Component, inject } from "@angular/core";
import { MaterialModule } from '../../../shared/material.module';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-theme-toggle',
  standalone: true,
  imports: [
    MaterialModule
  ],
  template: `
      <div class="fixed bottom-0 right-0">
        <button
          type="button"
          mat-icon-button
          class="m-1 p-0"
          (click)="themeService.toggleColorTheme()"
          [matTooltip]="themeService.isCurrentThemeDark() ? 'Claro' : 'Escuro'">
          @if (themeService.isCurrentThemeDark()) {
            <mat-icon class="font-icon text-amber-500">light_mode</mat-icon>
          } @else {
            <mat-icon class="font-icon text-sky-500">dark_mode</mat-icon>
          }
        </button>
      </div>
    `,
  styles: ``,
})
export class ThemeToggleComponent {
  public themeService = inject(ThemeService);
}