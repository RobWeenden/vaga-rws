import { Directive, ElementRef, inject, Input, Renderer2 } from '@angular/core';
import { StorageService } from '../../services/storage.service';

@Directive({
  selector: '[permissions]',
  standalone: true
})
export class PermissionsDirective {
  private readonly storageService = inject(StorageService);
  private readonly elementRef = inject(ElementRef);
  private readonly renderer = inject(Renderer2);
  private requiredPermissao: string[] = [];


  @Input() set permissions(permissao: string | string[]) {
    this.requiredPermissao = Array.isArray(permissao) ? permissao : [permissao];
    this.updateVisualizacao();
  }

  private updateVisualizacao(): void {
    const userRoles = this.storageService.getPermissoes();
    const hasAccess = this.requiredPermissao.some(role => userRoles.includes(role));

    if (!hasAccess) {
      this.renderer.removeChild(this.elementRef.nativeElement.parentNode, this.elementRef.nativeElement);
    }
  }

}
