import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { ConfirmationDialogComponent, ConfirmationDialogData } from './confirmation-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class ConfirmationDialogService {
  constructor(private readonly dialog: MatDialog) {}

  /**
   * Abre um diálogo de confirmação genérico
   * @param data Dados de configuração do diálogo
   * @returns Observable<boolean> - true se confirmado, false se cancelado
   */
  confirm(data: Partial<ConfirmationDialogData>): Observable<boolean> {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: 'auto',
      maxWidth: '95vw',
      panelClass: ['responsive-dialog'],
      disableClose: true, // Impede o fechamento ao clicar fora ou com ESC
      data: data
    });

    return dialogRef.afterClosed();
  }

  /**
   * Abre um diálogo de confirmação para exclusão
   * @param itemName Nome do item a ser excluído
   * @param message Mensagem personalizada (opcional)
   * @returns Observable<boolean> - true se confirmado, false se cancelado
   */
  confirmDelete(itemName: string, message?: string): Observable<boolean> {
    return this.confirm({
      title: `Excluir ${itemName}`,
      message: message || `Tem certeza que deseja excluir este item? Esta ação não pode ser desfeita.`,
      confirmText: 'Excluir',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'delete_forever'
    });
  }

  /**
   * Abre um diálogo de confirmação para desativar um item
   * @param itemName Nome do item a ser desativado
   * @param message Mensagem personalizada (opcional)
   * @returns Observable<boolean> - true se confirmado, false se cancelado
   */
  confirmDeactivate(itemName: string, message?: string): Observable<boolean> {
    return this.confirm({
      title: `Desativar ${itemName}`,
      message: message || `Tem certeza que deseja desativar este item?`,
      confirmText: 'Desativar',
      cancelText: 'Cancelar',
      type: 'warning',
      icon: 'block'
    });
  }

  /**
   * Abre um diálogo de confirmação para ativar um item
   * @param itemName Nome do item a ser ativado
   * @param message Mensagem personalizada (opcional)
   * @returns Observable<boolean> - true se confirmado, false se cancelado
   */
  confirmActivate(itemName: string, message?: string): Observable<boolean> {
    return this.confirm({
      title: `Ativar ${itemName}`,
      message: message || `Tem certeza que deseja ativar este item?`,
      confirmText: 'Ativar',
      cancelText: 'Cancelar',
      type: 'success',
      icon: 'check_circle'
    });
  }

  /**
   * Abre um diálogo de confirmação para qualquer ação potencialmente perigosa
   * @param action Nome da ação
   * @param message Mensagem personalizada (opcional)
   * @returns Observable<boolean> - true se confirmado, false se cancelado
   */
  confirmAction(action: string, message?: string): Observable<boolean> {
    return this.confirm({
      title: action,
      message: message || `Tem certeza que deseja ${action.toLowerCase()}?`,
      confirmText: 'Confirmar',
      cancelText: 'Cancelar',
      type: 'info',
      icon: 'help_outline'
    });
  }
}