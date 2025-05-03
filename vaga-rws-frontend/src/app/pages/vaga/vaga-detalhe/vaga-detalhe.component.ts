import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MaterialModule } from '../../../../shared/material.module';
import { ConfirmationDialogService } from '../../../components/confirmation-dialog/confirmation-dialog.service';
import { NotificationService } from '../../../components/notification/notification.service';
import { Candidatura } from '../../../core/models/candidatura';
import { Usuario } from '../../../core/models/usuario';
import { Vaga } from '../../../core/models/vaga';
import { CandidaturaService } from '../../../services/candidatura.service';
import { StorageService } from '../../../services/storage.service';
import { VagaService } from '../../../services/vaga.service';
import { getDiasRestantes } from '../../../core/util/data-util';

@Component({
  selector: 'app-vaga-detalhe',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MaterialModule
  ],
  templateUrl: './vaga-detalhe.component.html',
  styleUrls: ['./vaga-detalhe.component.scss']
})
export class VagaDetalheComponent implements OnInit {
  vaga: Vaga | null = null;
  carregando = true;
  erro = false;
  mensagemErro = '';
  jaCandidatou: boolean = false;
  
  podeEditar = true;
  idCandidaturaUsuarioLogado: any;
  vagaId:any;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly snackBar: MatSnackBar,
    private readonly confirmationService: ConfirmationDialogService,
    private readonly vagaService:VagaService,
    private readonly storageService: StorageService,
    private readonly notificationService: NotificationService,
    private readonly candidaturaService: CandidaturaService
  ) { }

  ngOnInit(): void {
   this.vagaId = this.route.snapshot.paramMap.get('id');
    
    if (!this.vagaId) {
      this.erro = true;
      this.mensagemErro = 'ID da vaga não encontrado.';
      this.carregando = false;
      return;
    }
    this.carregarVaga(this.vagaId);
  }
  
  carregarVaga(id: string): void {
    this.vagaService.buscarVagasByID(id)
    .subscribe((vaga: Vaga) => {
      if (vaga) {
        this.vaga = vaga;
        if (vaga.listaCandidatura) {
          vaga.listaCandidatura.forEach(candidatura => {
            if(candidatura.usuarioCandidato?.id === this.storageService.usuarioLogado().id) {
              this.idCandidaturaUsuarioLogado = candidatura.id;
              this.verificarCandidatura(true);
            }
          });
        }
        
        this.carregando = false;
      }
    });

    
  }
  
  verificarCandidatura(valor?:boolean): void {
    this.jaCandidatou = valor!;
  }
  
  candidatar(): void {
    if (!this.vaga) return;
    
    if (this.jaCandidatou) {
      this.snackBar.open('Você já se candidatou a esta vaga.', 'Fechar', {
        duration: 3000
      });
      return;
    }
    
    this.confirmationService.confirm({
      title: 'Confirmar candidatura',
      message: `Tem certeza que deseja se candidatar para a vaga de ${this.vaga.titulo} na empresa ${this.vaga.empresa}?`,
      confirmText: 'Sim, candidatar',
      cancelText: 'Cancelar',
      type: 'info',
      icon: 'person_add'
    }).subscribe(confirmado => {
      if (confirmado) {
        const usuarioLogado: Usuario = this.storageService.usuarioLogado();

        this.candidaturaService.candidatar(this.vaga?.id!, usuarioLogado.id)
        .subscribe((candidatura:Candidatura) => {
          if (candidatura) {
            this.carregando = false;
            this.carregarVaga(this.vagaId);
            this.verificarCandidatura(true);
            this.notificationService.showFeedback('Candidatura realizada com sucesso!', 'success');
          }
        });
        
      }
    });
  }
  
  // Implementação básica para compartilhamento
  compartilhar(): void {
    if (!this.vaga) return;
    if (navigator.share) {
      navigator.share({
        title: `Vaga: ${this.vaga.titulo} - ${this.vaga.empresa}`,
        text: `Confira esta vaga de ${this.vaga.titulo} na ${this.vaga.empresa}`,
        url: window.location.href
      }).catch((error) => {
        console.log('Erro ao compartilhar:', error);
      });
    } else {
      this.copiarLink();
    }
  }
  
  copiarLink(): void {
    const url = window.location.href;
    navigator.clipboard.writeText(url).then(() => {
      this.snackBar.open('Link copiado para a área de transferência!', 'Fechar', {
        duration: 2000
      });
    });
  }
   
  voltar(): void {
    this.router.navigate(['/vagas/busca']);
  }
  
  
  getDiasRestantes(data?: string | Date): number | null {
    return getDiasRestantes(data);
  }

  cancelarCandidatura() {

    this.confirmationService.confirm({
      title: 'Cancelar candidatura',
      message: `Tem certeza que deseja cancelar sua candidatura para a vaga de ${this.vaga?.titulo} na empresa ${this.vaga?.empresa}?`,
      confirmText: 'Sim,',
      cancelText: 'Não',
      type: 'info',
      icon: 'person_add'
    }).subscribe(confirmado => {
      if (confirmado) {
        this.candidaturaService.deletar(this.idCandidaturaUsuarioLogado)
        .subscribe({
          next: (result) => {
            this.notificationService.showFeedback(`${result.message}`, 'success');
            this.carregarVaga(this.vagaId);
            this.verificarCandidatura(false);
          },
          error: (error) => {
            this.notificationService.showFeedback('Error ao tentar cancelar candidatura', 'error');
          },
        });
        
      }
    });

  }
}