import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit, ViewEncapsulation } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBarRef } from '@angular/material/snack-bar';
import { MaterialModule } from '../../../shared/material.module';
import { NotificationType } from './notification.service';

interface NotificationData {
  message: string;
  type: NotificationType;
}

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [
    CommonModule,
    MaterialModule
  ],
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class NotificationComponent implements OnInit {
  icon: string = 'info';

  constructor(
    public snackBarRef: MatSnackBarRef<NotificationComponent>,
    @Inject(MAT_SNACK_BAR_DATA) public data: NotificationData
  ) { }

  ngOnInit(): void {
    this.setIcon();
  }

  /**
   * Define o ícone com base no tipo de notificação
   */
  setIcon(): void {
    switch (this.data.type) {
      case 'success':
        this.icon = 'check_circle';
        break;
      case 'error':
        this.icon = 'error';
        break;
      case 'warning':
        this.icon = 'warning';
        break;
      case 'info':
      default:
        this.icon = 'info';
        break;
    }
  }

  /**
   * Fecha a notificação
   */
  dismiss(): void {
    this.snackBarRef.dismiss();
  }
}