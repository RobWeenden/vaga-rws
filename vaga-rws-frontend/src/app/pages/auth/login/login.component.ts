import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { MaterialModule } from '../../../../shared/material.module';
import { LoginRequest } from '../../../core/models/login-request.component';
import { AuthService } from '../../../services/auth.service';
import { StorageService } from '../../../services/storage.service';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  encapsulation: ViewEncapsulation.None,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    MaterialModule
  ],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  carregando = false;
  mostrarSenha = false;
  erroLogin = false;
  mensagemErro = '';

  constructor(
    private readonly fb: FormBuilder,
    private readonly router: Router,
    private readonly snackBar: MatSnackBar,
    private readonly authService: AuthService,
    private readonly storageService: StorageService
  ) { }

  ngOnInit(): void {
    this.inicializarFormulario();

  }

  inicializarFormulario(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required]],
      lembrar: [false]
    });
  }

  toggleMostrarSenha(): void {
    this.mostrarSenha = !this.mostrarSenha;
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      Object.keys(this.loginForm.controls).forEach(key => {
        const control = this.loginForm.get(key);
        control?.markAsTouched();
      });
      return;
    }

    this.carregando = true;
    this.erroLogin = false;

    const { email, senha, lembrarMe } = this.loginForm.value;
    const loginRequest = new LoginRequest(email, senha, lembrarMe);

    this.authService.login(loginRequest)
      .subscribe({
        next: () => {
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          this.loginForm.reset();
          this.storageService.setAuthenticated(false);
        },
      });

  }

  hasError(controlName: string, errorName: string): boolean {
    const control = this.loginForm.get(controlName);
    return (control?.touched || control?.dirty) && control?.hasError(errorName) || false;
  }

  irParaRecuperarSenha(): void {
    this.router.navigate(['/recuperar-senha']);
  }

  irParaCadastro(): void {
    this.router.navigate(['/cadastro']);
  }



}
