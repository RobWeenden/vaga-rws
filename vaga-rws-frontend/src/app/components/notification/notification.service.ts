import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NotificationComponent } from './notification.component';

export type NotificationType = 'success' | 'error' | 'info' | 'warning';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private readonly snackBar: MatSnackBar) { }

  showFeedback(message: string, type: 'success' | 'info' | 'warning' | 'error'): void {
    this.snackBar.openFromComponent(NotificationComponent, {
      data: { message, type },
      duration: 100000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['notification-snackbar', 'box-shadow:none']
    });
  }

}