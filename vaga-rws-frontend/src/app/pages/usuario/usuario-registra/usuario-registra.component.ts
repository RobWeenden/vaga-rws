import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NgxMaskDirective } from 'ngx-mask';
import { MaterialModule } from '../../../../shared/material.module';
import { Usuario } from '../../../core/models/usuario';
import { PerfilUsuarioEnum } from '../../../core/models/usuario-perfil.enum';
import { senhasConferemValidator } from '../../../core/validation/password-validation';


@Component({
  selector: 'app-usuario-registra',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    NgxMaskDirective,
  ],
  templateUrl: './usuario-registra.component.html',
  styleUrls: ['./usuario-registra.component.scss']
})
export class UsuarioRegistraComponent implements OnInit, AfterViewInit {
  usuarioForm!: FormGroup;
  perfilOptions = Object.values(PerfilUsuarioEnum);

  modoEdicao: boolean = false;
  mostrarSenha: boolean = false;
  mostrarConfirmarSenha: boolean = false;

  constructor(
    private readonly fb: FormBuilder,
    public dialogRef: MatDialogRef<UsuarioRegistraComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Usuario,
  ) {
    this.modoEdicao = !!data?.id;
    this.configDialogTelas();
  }


  ngOnInit() {
    this.inicializarFormulario();
  }

  ngAfterViewInit() {
    setTimeout(() => {
      const dialogElement = document.querySelector('.mat-mdc-dialog-container') as HTMLElement;
      if (dialogElement) {
        dialogElement.style.maxHeight = '90vh';
        dialogElement.style.overflow = 'hidden';
      }
    }, 100);
  }


  configDialogTelas() {
    // Configuração para diálogo responsivo
    this.dialogRef.updateSize('95%', '90%');

    // Ajustar o tamanho do diálogo para telas maiores
    const setDialogSize = () => {
      const width = window.innerWidth;
      if (width >= 1024) {
        this.dialogRef.updateSize('800px', '90%');
      } else if (width >= 768) {
        this.dialogRef.updateSize('90%', '90%');
      } else {
        this.dialogRef.updateSize('95%', '90%');
      }
    };
    setDialogSize();
    window.addEventListener('resize', setDialogSize);

  }

  inicializarFormulario(): void {
    if (this.modoEdicao) {
      this.usuarioForm = this.fb.group({
        id: [this.data.id],
        nome: [this.data.nome, [Validators.required]],
        email: [this.data.email, [Validators.required, Validators.email]],
        senha: [''],
        confirmarSenha: [''],
        telefone: [this.data.telefone],
        departamento: [this.data.departamento],
        cargo: [this.data.cargo],
        perfil: [this.data.perfil, [Validators.required]],
        ativo: [this.data.ativo]
      },  { validators: senhasConferemValidator});

    } else {
      this.usuarioForm = this.fb.group({
        id: [''],
        nome: ['', [Validators.required]],
        email: ['', [Validators.required, Validators.email]],
        senha: ['', [Validators.required, Validators.minLength(6)]],
        confirmarSenha: ['', [Validators.required]],
        telefone: [''],
        departamento: [''],
        cargo: [''],
        perfil: [PerfilUsuarioEnum.COLABORADOR, [Validators.required]]
      }, { validators: senhasConferemValidator});
    }
  }

  toggleMostrarSenha(): void {
    this.mostrarSenha = !this.mostrarSenha;
  }

  toggleMostrarConfirmarSenha(): void {
    this.mostrarConfirmarSenha = !this.mostrarConfirmarSenha;
  }

  onSubmit(): void {
    if (this.usuarioForm.invalid) {
      Object.keys(this.usuarioForm.controls).forEach(key => {
        const control = this.usuarioForm.get(key);
        control?.markAsTouched();
      });
      return;
    }

    const usuarioData = { ...this.usuarioForm.value };
    if (this.modoEdicao && !usuarioData.senha) {
      delete usuarioData.senha;
      delete usuarioData.confirmarSenha;
    }

    delete usuarioData.confirmarSenha;
    this.dialogRef.close(usuarioData);
  }

  fechar(): void {
    this.dialogRef.close();
  }

  hasError(controlName: string, errorName: string): boolean {
    const control = this.usuarioForm.get(controlName);
    return (control?.touched || control?.dirty) && control?.hasError(errorName) || false;
  }

  hasSenhasError(): boolean {
    return (this.usuarioForm.touched || this.usuarioForm.dirty) &&
      this.usuarioForm.hasError('senhasNaoConferem');
  }
}