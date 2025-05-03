import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgxMaskDirective } from 'ngx-mask';
import { MaterialModule } from '../../../../shared/material.module';
import { NotificationService } from '../../../components/notification/notification.service';
import { Usuario } from '../../../core/models/usuario';
import { PerfilUsuarioEnum } from '../../../core/models/usuario-perfil.enum';
import { senhasConferemValidator } from '../../../core/validation/password-validation';
import { UsuarioService } from '../../../services/usuario.service';


@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    NgxMaskDirective,
  ],
  templateUrl: './cadastro.component.html',
  styleUrls: ['./cadastro.component.scss']
})
export class CadastroComponent implements OnInit {
  usuarioForm!: FormGroup;
  perfilOptions = Object.values(PerfilUsuarioEnum);

  modoEdicao: boolean = false;
  mostrarSenha: boolean = false;
  mostrarConfirmarSenha: boolean = false;

  constructor(
    private readonly fb: FormBuilder,
    private readonly usuarioSevice: UsuarioService,
    private readonly notificationService: NotificationService,
    private readonly router: Router
  ) {
  }


  ngOnInit() {
    this.inicializarFormulario();
  }


  inicializarFormulario(): void {
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
    }, { validators: senhasConferemValidator });

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
    this.cadastrarUsuario(usuarioData);

  }

  hasError(controlName: string, errorName: string): boolean {
    const control = this.usuarioForm.get(controlName);
    return (control?.touched || control?.dirty) && control?.hasError(errorName) || false;
  }

  hasSenhasError(): boolean {
    return (this.usuarioForm.touched || this.usuarioForm.dirty) &&
      this.usuarioForm.hasError('senhasNaoConferem');
  }

  cadastrarUsuario(usuario: Usuario) {
    this.usuarioSevice.criarUsuario(usuario)
      .subscribe({
        next: (usuario) => {
          this.notificationService.showFeedback('Cadastrado com sucesso', 'success');
          this.router.navigate(['/login']);
        }, error: (error) => {
          this.notificationService.showFeedback(`${error.error.errorMessage}`, 'success');
        }
      });
  }

  fechar(){
    this.router.navigate(['/login']);
  }
}