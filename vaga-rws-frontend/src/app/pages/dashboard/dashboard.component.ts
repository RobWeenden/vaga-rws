import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../../shared/material.module';
import { StorageService } from '../../services/storage.service';
import { CandidaturaService } from '../../services/candidatura.service';
import { Candidatura } from '../../core/models/candidatura';

@Component({
  selector: 'app-dashboard',
  imports: [
    CommonModule,
    RouterModule,
    MaterialModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {

  totalCandidaturas: number = 0;
  visualizacoesPerfil: number = 0;
  idUsuarioLogado: string = '';

  constructor(
    private readonly storageService: StorageService,
    private readonly candidaturaService: CandidaturaService,
  ) { }

  ngOnInit(): void {
    this.idUsuarioLogado = this.storageService.usuarioLogado().id;

    if (this.possuiPermissaoAdmin()) {
      this.buscarCandidaturas();
    } else if (this.possuiPermissaoResponsavel()) {
      this.buscarCandidaturasUsuarioResponsavel(this.idUsuarioLogado);
    } else {
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
          this.totalCandidaturas = candidaturas.length;
          this.visualizacoesPerfil = candidaturas.length;
        }
      })
  }

  buscarCandidaturasUsuarioResponsavel(idUsuarioResponsavel: string) {
    this.candidaturaService.buscarCandidaturasByIdUsuarioResponsavel(idUsuarioResponsavel)
      .subscribe((candidaturas: Candidatura[]) => {
        if (candidaturas) {
          this.totalCandidaturas = candidaturas.length;
          this.visualizacoesPerfil = candidaturas.length;
        }
      })
  }

  carregarCandidaturasUsuarioCandidato(idUsuarioCandidato: string): void {

    this.candidaturaService.buscarCandidaturasByIdUsuarioCandidato(idUsuarioCandidato)
      .subscribe((candidaturas: Candidatura[]) => {
        if (candidaturas) {
          this.totalCandidaturas = candidaturas.length;
          this.visualizacoesPerfil = candidaturas.length;
        }
      })


  }
}
