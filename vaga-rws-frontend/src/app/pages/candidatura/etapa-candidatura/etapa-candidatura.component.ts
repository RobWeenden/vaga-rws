import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../../../shared/material.module';
import { StatusCandidaturaEnum } from '../../../core/models/status-candidatura.enum';

interface EtapaProcesso {
  nome: string;
  status: StatusCandidaturaEnum;
}

@Component({
  selector: 'app-etapa-candidatura',
  standalone: true,
  imports: [
    CommonModule, 
    MaterialModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl: './etapa-candidatura.component.html',
  styleUrls: ['./etapa-candidatura.component.scss']
})
export class EtapaCandidaturaComponent implements OnInit {

  etapaForm!: FormGroup;
  etapasDisponiveis: EtapaProcesso[] = [
    { nome: 'Triagem inicial', status: StatusCandidaturaEnum.TRIAGEM },
    { nome: 'Entrevista RH', status: StatusCandidaturaEnum.ENTREVISTA },
    { nome: 'Teste técnico', status: StatusCandidaturaEnum.ENTREVISTA },
    { nome: 'Entrevista técnica', status: StatusCandidaturaEnum.ENTREVISTA },
    { nome: 'Entrevista final', status: StatusCandidaturaEnum.ENTREVISTA },
    { nome: 'Proposta', status: StatusCandidaturaEnum.APROVADA },
    { nome: 'Contratação', status: StatusCandidaturaEnum.APROVADA }
  ];

  constructor(
    private readonly fb: FormBuilder,
    public dialogRef: MatDialogRef<EtapaCandidaturaComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { 
      candidaturaId: number, 
      etapaAtual: string,
      etapas: any[]
    }
  ) { }

  ngOnInit(): void {
    this.etapaForm = this.fb.group({
      etapaAtualConcluida: [false, Validators.required],
      proximaEtapa: ['', Validators.required]
    });

    this.etapasDisponiveis = this.etapasDisponiveis.filter(
      etapa => etapa.nome !== this.data.etapaAtual
    );
  }

  onSubmit(): void {
    if (this.etapaForm.valid) {
      
      const proximaEtapaSelecionada = this.etapasDisponiveis.find(
        etapa => etapa.nome === this.etapaForm.value.proximaEtapa
      );

      const formData = {
        candidaturaId: this.data.candidaturaId,
        etapaAtualConcluida: this.etapaForm.value.etapaAtualConcluida,
        proximaEtapa: this.etapaForm.value.proximaEtapa,
        statusProximaEtapa: proximaEtapaSelecionada ? proximaEtapaSelecionada.status : undefined
      };
      this.dialogRef.close(formData);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

}