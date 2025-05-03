import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MaterialModule } from '../../../../shared/material.module';
import { ModalidadeVagaEnum } from '../../../core/models/modalidade-vaga.enum';
import { RegimeEnum } from '../../../core/models/regime.enum';
import { StatusVagaEnum } from '../../../core/models/status-vaga.enum';
import { Usuario } from '../../../core/models/usuario';
import { Vaga } from '../../../core/models/vaga';
import { UsuarioService } from '../../../services/usuario.service';

@Component({
  selector: 'app-vaga-registra',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule
  ],
  templateUrl: './vaga-registra.component.html',
  styleUrls: ['./vaga-registra.component.scss']
})
export class VagaRegistraComponent implements OnInit, AfterViewInit {
  vagaForm!: FormGroup;
  regimeOptions = Object.values(RegimeEnum);
  modalidadeOptions = Object.values(ModalidadeVagaEnum);
  statusOptions = Object.values(StatusVagaEnum);
  listaUsuarioResponsavel: Usuario[] = [];

  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  requisitos: string[] = [];
  beneficios: string[] = [];
  diferenciais: string[] = [];

  modoEdicao: boolean = false;
  sobreEmpresaTextPadrao = 'A TechCorp Solutions é uma empresa de tecnologia em crescimento, focada no desenvolvimento de soluções inovadoras para o mercado corporativo. Com mais de 10 anos de experiência no mercado, temos uma cultura de inovação, colaboração e excelência técnica.';


  constructor(
    private readonly fb: FormBuilder,
    public dialogRef: MatDialogRef<VagaRegistraComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Vaga,
    private readonly usuarioSevice: UsuarioService,
  ) {
    this.modoEdicao = !!data?.id;
    this.requisitos = data?.requisitos || [];
    this.beneficios = data?.beneficios || [];
    this.diferenciais = data?.diferenciais || [];

    this.dialogRef.updateSize('95%', '90%');

    // Ajustar o tamanho do diálogo para telas maiores
    const setDialogSize = () => {
      const width = window.innerWidth;
      if (width >= 1024) { // lg breakpoint
        this.dialogRef.updateSize('800px', '90%');
      } else if (width >= 768) { // md breakpoint
        this.dialogRef.updateSize('90%', '90%');
      } else { // Telas pequenas
        this.dialogRef.updateSize('95%', '90%');
      }
    }
    // Configurar tamanho inicial
    setDialogSize();

    // Adicionar listener para redimensionamento da janela
    window.addEventListener('resize', setDialogSize);
  }

  ngOnInit() {
    this.inicializarFormulario();
    this.buscarUsuariosSemPerfilColaborador();
  }

  ngAfterViewInit() {
    // Garantir que o diálogo se ajuste após renderização completa
    setTimeout(() => {
      const dialogElement = document.querySelector('.mat-mdc-dialog-container') as HTMLElement;
      if (dialogElement) {
        dialogElement.style.maxHeight = '90vh';
        dialogElement.style.overflow = 'hidden';
      }
    }, 100);
  }

  inicializarFormulario(): void {
    this.vagaForm = this.fb.group({
      id: [this.data?.id],
      titulo: [this.data?.titulo || '', [Validators.required]],
      empresa: [this.data?.empresa || '', [Validators.required]],
      localidade: [this.data?.localidade || '', [Validators.required]],
      regime: [this.data?.regime || 'CLT', [Validators.required]],
      modalidade: [this.data?.modalidade || 'Presencial', [Validators.required]],
      faixaSalarial: [this.data?.faixaSalarial || ''],
      descricao: [this.data?.descricao || '', [Validators.required, Validators.minLength(20)]],
      dataPublicacao: [this.data?.dataPublicacao ? this.formatDateForInput(this.data.dataPublicacao) : this.formatDateForInput(new Date())],
      dataPrazoInscricao: [this.data?.dataPrazoInscricao ? this.formatDateForInput(this.data.dataPrazoInscricao) : this.formatDateForInput(new Date())],
      status: [this.data?.status || 'Ativa'],
      emailContato: [this.data.emailContato, [Validators.required, Validators.email]],
      logoEmpresa: 'assets/logos/techcorp.png',
      sobreEmpresa: this.sobreEmpresaTextPadrao,
      qtdCandidaturas: [this.data?.qtdCandidaturas || 0],
      usuarioResponsavel: [this.data?.usuarioResponsavel],
      usuarioResponsavelID: ['']
    });
  }

  adicionarRequisitoManual(valor: string): void {
    const value = valor.trim();

    if (value && !this.requisitos.includes(value)) {
      this.requisitos.push(value);
      this.vagaForm.markAsTouched();
    }
  }

  adicionarBeneficioManual(valor: string): void {
    const value = valor.trim();

    if (value && !this.beneficios.includes(value)) {
      this.beneficios.push(value);
      this.vagaForm.markAsTouched();
    }
  }

  adicionarDiferencialManual(valor: string): void {
    const value = valor.trim();

    if (value && !this.diferenciais.includes(value)) {
      this.diferenciais.push(value);
      this.vagaForm.markAsTouched();
    }
  }


  removerRequisito(requisito: string): void {
    const index = this.requisitos.indexOf(requisito);

    if (index >= 0) {
      this.requisitos.splice(index, 1);
    }
  }


  removerBeneficio(beneficios: string): void {
    const index = this.beneficios.indexOf(beneficios);

    if (index >= 0) {
      this.beneficios.splice(index, 1);
    }
  }

  removerDiferencial(diferencial: string): void {
    const index = this.diferenciais.indexOf(diferencial);

    if (index >= 0) {
      this.diferenciais.splice(index, 1);
    }
  }

  onSubmit(): void {
    if (this.vagaForm.invalid) {
      Object.keys(this.vagaForm.controls).forEach(key => {
        const control = this.vagaForm.get(key);
        control?.markAsTouched();
      });
      return;
    }

    if (this.requisitos.length === 0) {
      alert('Por favor, adicione pelo menos um requisito para a vaga.');
      return;
    }

    const vagaData = {
      ...this.vagaForm.value,
      requisitos: this.requisitos,
      beneficios: this.beneficios,
      diferenciais: this.diferenciais,
      qtdCandidaturas: this.data?.qtdCandidaturas || 0,
      usuarioResponsavelID: this.vagaForm.get('usuarioResponsavel')?.value.id
    };

    this.dialogRef.close(vagaData);
  }

  fechar(): void {
    this.dialogRef.close();
  }

  hasError(controlName: string, errorName: string): boolean {
    const control = this.vagaForm.get(controlName);
    return (control?.touched || control?.dirty) && control?.hasError(errorName) || false;
  }

  formatDateForInput(date: Date): string {
    const d = new Date(date);
    let month = '' + (d.getMonth() + 1);
    let day = '' + d.getDate();
    const year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
  }

  buscarUsuariosSemPerfilColaborador(): void {
    this.usuarioSevice.buscarUsuariosSemPerfilColaborador()
      .subscribe((usuarios: Usuario[]) => {
        if (usuarios) {
          this.listaUsuarioResponsavel = usuarios;
        }
      });
  }

  compararUsuarios(usuario1: Usuario, usuario2: Usuario): boolean {
    return usuario1 && usuario2 ? usuario1.id === usuario2.id : usuario1 === usuario2;
  }
}
