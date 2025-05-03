import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MaterialModule } from '../../../../shared/material.module';
import { NotificationService } from '../../../components/notification/notification.service';
import { ModalidadeVagaEnum } from '../../../core/models/modalidade-vaga.enum';
import { Vaga } from '../../../core/models/vaga';
import { VagaService } from '../../../services/vaga.service';

@Component({
  selector: 'app-vaga-search',
  imports: [
    CommonModule, 
    MaterialModule,
    FormsModule
  ],
  templateUrl: './vaga-search.component.html',
  styleUrl: './vaga-search.component.scss'
})
export class VagaSearchComponent {
  termoBusca: string = '';
  vagasEncontradas: Vaga[] = [];
  buscaRealizada: boolean = false;
  modalidadesEnum = ModalidadeVagaEnum;

  constructor(
    private readonly router: Router,
    private readonly vagaService: VagaService,
    private readonly notificationService: NotificationService
  ) {}

  buscarVagas() {
    if (this.termoBusca.trim() === '') return;
    this.buscaRealizada = true;
    this.vagaService.buscarVagasByTitulo(this.termoBusca)
    .subscribe((vagas:Vaga[]) => {
      if (vagas && vagas.length < 1) {
        this.notificationService.showFeedback('Nenhum resultado encontrado.', 'info');
      }
      this.vagasEncontradas = vagas
    });
  }

  receberVagasWhatsapp() {
    alert('Você se inscreveu para receber vagas pelo WhatsApp!');
  }

  verDetalhes(vaga: Vaga) {
    this.router.navigate(['/vagas/detalhe', vaga.id]);
  }

}
