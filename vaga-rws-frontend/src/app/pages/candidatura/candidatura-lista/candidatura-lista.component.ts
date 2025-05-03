import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router, RouterModule } from '@angular/router';
import { MaterialModule } from '../../../../shared/material.module';
import { Candidatura } from '../../../core/models/candidatura';
import { EtapaCandidatura } from '../../../core/models/etapa-candidatura';
import { StatusCandidaturaEnum } from '../../../core/models/status-candidatura.enum';
import { TipoFiltroEnum } from '../../../core/models/tipo-filtro.enum';
import { CandidaturaService } from '../../../services/candidatura.service';
import { StorageService } from '../../../services/storage.service';
import { EtapaCandidaturaComponent } from '../etapa-candidatura/etapa-candidatura.component';


@Component({
  selector: 'app-candidatura-lista',
  standalone: true,
  imports: [
    CommonModule, 
    MaterialModule,
    RouterModule
  ],
  templateUrl: './candidatura-lista.component.html',
  styleUrls: ['./candidatura-lista.component.scss']
})
export class CandidaturaListaComponent implements OnInit {

  todasCandidaturas: Candidatura[] = [];
  candidaturas: Candidatura[] = [];
  temCandidaturas: boolean = false;
  tabSelecionada: TipoFiltroEnum = TipoFiltroEnum.TODAS;
  
  contadorEmTriagem: number = 0;
  contadorEmAndamento: number = 0;
  contadorFinalizadas: number = 0;
  idUsuarioLogado:string= '';

  constructor(
    private readonly storageService: StorageService,
    private readonly candidaturaService : CandidaturaService,
    private readonly router: Router,
    private readonly dialog: MatDialog
  ) { }

  ngOnInit(): void {

    this.idUsuarioLogado = this.storageService.usuarioLogado().id;
    this.carregarCandidaturas();

    
  }

  carregarCandidaturas() {
    if (this.possuiPermissaoAdmin()) {
      this.buscarCandidaturas();
    } else if (this.possuiPermissaoResponsavel()) {
      this.buscarCandidaturasUsuarioResponsavel(this.idUsuarioLogado);
    }else {
      this.carregarCandidaturasUsuarioCandidato(this.idUsuarioLogado);
    }
  }
  
  possuiPermissaoAdmin() {
    return this.storageService.possuiPermissionAdmin();
  }

  possuiPermissaoResponsavel() {
    return this.storageService.possuiPermissionResponsavel()
  }

  buscarCandidaturas() {
    this.candidaturaService.buscarCandidaturas()
    .subscribe((candidaturas: Candidatura[]) => {
      if (candidaturas) {
        this.todasCandidaturas = candidaturas;
        this.temCandidaturas = this.todasCandidaturas.length > 0;

        this.filtrarCandidaturas();
        this.calcularContadores();
      }
    })
  }

  buscarCandidaturasUsuarioResponsavel(idUsuarioResponsavel:string) {
    this.candidaturaService.buscarCandidaturasByIdUsuarioResponsavel(idUsuarioResponsavel)
    .subscribe((candidaturas: Candidatura[]) => {
      if (candidaturas) {
        this.todasCandidaturas = candidaturas;
        this.temCandidaturas = this.todasCandidaturas.length > 0;

        this.filtrarCandidaturas();
        this.calcularContadores();
      }
    })
  }
  
  carregarCandidaturasUsuarioCandidato(idUsuarioCandidato: string): void {

    this.candidaturaService.buscarCandidaturasByIdUsuarioCandidato(idUsuarioCandidato)
    .subscribe((candidaturas: Candidatura[]) => {
      if (candidaturas) {
        this.todasCandidaturas = candidaturas;
        this.temCandidaturas = this.todasCandidaturas.length > 0;

        this.filtrarCandidaturas();
        this.calcularContadores();
      }
    });
  }
  

  filtrarCandidaturas(): void {
    switch (this.tabSelecionada) {
      case TipoFiltroEnum.TODAS:
        this.candidaturas = [...this.todasCandidaturas];
        break;
        
      case TipoFiltroEnum.EM_ANDAMENTO:
        this.candidaturas = this.todasCandidaturas.filter(c => 
          c.status === StatusCandidaturaEnum.PENDENTE || 
          c.status === StatusCandidaturaEnum.VISUALIZADA || 
          c.status === StatusCandidaturaEnum.ENTREVISTA
        );
        break;
        
      case TipoFiltroEnum.FINALIZADAS:
        this.candidaturas = this.todasCandidaturas.filter(c => 
          c.status === StatusCandidaturaEnum.APROVADA || 
          c.status === StatusCandidaturaEnum.REPROVADA
        );
        break;
    }
  }
  

  calcularContadores(): void {
    this.contadorEmTriagem = this.todasCandidaturas.filter(c => 
      c.status === StatusCandidaturaEnum.TRIAGEM
    ).length;

    this.contadorEmAndamento = this.todasCandidaturas.filter(c => 
      c.status === StatusCandidaturaEnum.PENDENTE || 
      c.status === StatusCandidaturaEnum.VISUALIZADA || 
      c.status === StatusCandidaturaEnum.ENTREVISTA
    ).length;
    
    this.contadorFinalizadas = this.todasCandidaturas.filter(c => 
      c.status === StatusCandidaturaEnum.APROVADA || 
      c.status === StatusCandidaturaEnum.REPROVADA
    ).length;
  }
  

  onTabChange(index: number): void {
    this.tabSelecionada = index as TipoFiltroEnum;
    this.filtrarCandidaturas();
  }


  getStatusClass(status: string): string {
    switch (status) {
      case StatusCandidaturaEnum.TRIAGEM: return 'status-pendente';
      case StatusCandidaturaEnum.PENDENTE: return 'status-pendente';
      case StatusCandidaturaEnum.VISUALIZADA: return 'status-visualizada';
      case StatusCandidaturaEnum.ENTREVISTA: return 'status-entrevista';
      case StatusCandidaturaEnum.APROVADA: return 'status-aprovada';
      case StatusCandidaturaEnum.REPROVADA: return 'status-reprovada';
      default: return '';
    }
  }
  

  getStatusText(status: string): string {
    switch (status) {
      case StatusCandidaturaEnum.TRIAGEM: return 'Triagem';
      case StatusCandidaturaEnum.PENDENTE: return 'Pendente';
      case StatusCandidaturaEnum.VISUALIZADA: return 'CV Visualizado';
      case StatusCandidaturaEnum.ENTREVISTA: return 'Entrevista agendada';
      case StatusCandidaturaEnum.APROVADA: return 'Aprovado';
      case StatusCandidaturaEnum.REPROVADA: return 'Não selecionado';
      default: return status;
    }
  }


  buscarVagas(): void {
    this.router.navigate(['vagas/busca']);
  }

  atualizarEtapa(candidatura:any) {
    const dialogRef = this.dialog.open(EtapaCandidaturaComponent, {
      width: '500px',
      data: {
        candidaturaId: candidatura.id,
        etapaAtual: candidatura.etapaAtual,
        etapas: candidatura.etapas
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        let etapaCandidatura: EtapaCandidatura = {
          etapaAtualConcluida: result.etapaAtualConcluida,
          proximaEtapa: result.proximaEtapa,
          candidaturaId: result.candidaturaId,
          statusProximaEtapa: result.statusProximaEtapa
        }
        this.candidaturaService.atualizarEtapa(etapaCandidatura).subscribe({
          next: (response) => {
            this.carregarCandidaturas();
          },
          error: (error) => {
          }
        });
      }
    });
  }

}