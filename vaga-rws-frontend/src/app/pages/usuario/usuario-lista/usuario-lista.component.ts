import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxMaskPipe } from 'ngx-mask';
import { MaterialModule } from '../../../../shared/material.module';
import { ConfirmationDialogService } from '../../../components/confirmation-dialog/confirmation-dialog.service';
import { NotificationService } from '../../../components/notification/notification.service';
import { Usuario } from '../../../core/models/usuario';
import { PerfilUsuarioEnum } from '../../../core/models/usuario-perfil.enum';
import { UsuarioService } from '../../../services/usuario.service';
import { UsuarioRegistraComponent } from '../usuario-registra/usuario-registra.component';

@Component({
  selector: 'app-usuario-lista',
  standalone: true,
  imports: [
    CommonModule,
    MaterialModule,
    NgxMaskPipe
  ],
  templateUrl: './usuario-lista.component.html',
  styleUrls: ['./usuario-lista.component.scss']
})
export class UsuarioListaComponent implements OnInit {
  displayedColumns: string[] = ['nome', 'email', 'departamento', 'cargo', 'perfil', 'ativo', 'dataCadastro', 'acoes'];
  dataSource = new MatTableDataSource<any>([]);
  totalUsuarios = 0;
  usuariosCompletos: Usuario[] = [];

  pageSize = 5;
  pageSizeOptions: number[] = [5, 10, 25, 50];

  PerfilUsuario = PerfilUsuarioEnum;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  perfilAtual = PerfilUsuarioEnum.ADMINISTRADOR;

  constructor(
    private readonly dialog: MatDialog,
    private readonly confirmationService: ConfirmationDialogService,
    private readonly usuarioSevice: UsuarioService,
    private readonly notificacaoService: NotificationService
  ) { }

  ngOnInit(): void {
    this.buscarUsuarios();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    this.dataSource.filterPredicate = (data: any, filter: string) => {
      const searchTerm = filter.toLowerCase();
      return data.nome.toLowerCase().includes(searchTerm) ||
        data.email.toLowerCase().includes(searchTerm) ||
        (data.departamento && data.departamento.toLowerCase().includes(searchTerm)) ||
        (data.cargo && data.cargo.toLowerCase().includes(searchTerm));
    };
  }

  buscarUsuarios(): void {
    this.usuarioSevice.buscarUsuarios()
      .subscribe((usuarios: Usuario[]) => {
        if (usuarios) {
          this.usuariosCompletos = usuarios;
          this.totalUsuarios = this.usuariosCompletos.length;
          this.dataSource.data = this.usuariosCompletos;
        }
      });

  }

  mudarPagina(event: PageEvent): void {
    this.pageSize = event.pageSize;
  }

  ordenar(event: Sort): void {
    const data = this.usuariosCompletos.slice();
    if (!event.active || event.direction === '') {
      this.dataSource.data = data;
      return;
    }

    this.dataSource.data = data.sort((a, b) => {
      const isAsc = event.direction === 'asc';
      switch (event.active) {
        case 'nome': return this.comparar(a.nome, b.nome, isAsc);
        case 'email': return this.comparar(a.email, b.email, isAsc);
        case 'departamento': return this.comparar(a.departamento || '', b.departamento || '', isAsc);
        case 'cargo': return this.comparar(a.cargo || '', b.cargo || '', isAsc);
        case 'perfil': return this.comparar(a.perfil, b.perfil, isAsc);
        case 'ativo': return this.comparar(a.ativo, b.ativo, isAsc);
        default: return 0;
      }
    });
  }

  private comparar(
    a: number | string | boolean | undefined | null,
    b: number | string | boolean | undefined | null,
    isAsc: boolean
  ): number {
    if (a == null && b == null) return 0;
    if (a == null) return isAsc ? -1 : 1;
    if (b == null) return isAsc ? 1 : -1;
    return (a < b ? -1 : a > b ? 1 : 0) * (isAsc ? 1 : -1);
  }

  aplicarFiltro(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  alterarStatusUsuario(usuario: Usuario): void {
    if (this.perfilAtual !== this.PerfilUsuario.ADMINISTRADOR) {
      this.notificacaoService.showFeedback('Apenas administradores podem alterar o status de usuários.', 'warning');
      return;
    }

    const index = this.usuariosCompletos.findIndex(u => u.id === usuario.id);
    if (index !== -1) {
      const novoStatus = !usuario.ativo;
      usuario = { ...usuario, ativo: novoStatus };
      this.atualizarUsuario(usuario, index);
    }
  }

  abrirDialogUsuario(usuario?: Usuario): void {

    if (this.perfilAtual !== this.PerfilUsuario.ADMINISTRADOR) {
      this.notificacaoService.showFeedback('Apenas administradores podem gerenciar usuários.', 'warning');
      return;
    }

    const dialogConfig = {
      width: '95%',
      maxWidth: '800px',
      maxHeight: '90vh',
      panelClass: ['responsive-dialog'],
      data: usuario ? { ...usuario } : {}
    };

    const dialogRef = this.dialog.open(UsuarioRegistraComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (result.id) {
          const index = this.usuariosCompletos.findIndex(u => u.id === result.id);
          if (index !== -1) {
            this.atualizarUsuario(result, index);
          }
        } else {
          this.cadastrarUsuario(result);
        }
      }
    });
  }


  excluirUsuario(id: string, nome: string): void {
    if (this.perfilAtual !== this.PerfilUsuario.ADMINISTRADOR) {
      this.notificacaoService.showFeedback('Apenas administradores podem excluir usuários.', 'warning');
      return;
    }
    this.confirmationService.confirmDelete('usuário', `Tem certeza que deseja excluir o usuário "${nome}"? Esta ação não pode ser desfeita.`)
      .subscribe(confirmado => {
        if (confirmado) {
          this.deletarUsuario(id);
        }
      });
  }


  cadastrarUsuario(usuario: Usuario) {
    this.usuarioSevice.criarUsuario(usuario)
      .subscribe({
        next: (usuario: Usuario) => {
          this.usuariosCompletos.push(usuario);
          this.totalUsuarios = this.usuariosCompletos.length;
          this.atualizarDadosTabela();
          this.notificacaoService.showFeedback('Cadastrado com sucesso', 'success');
        }, error: (error) => {
          this.notificacaoService.showFeedback(`${error.error.errorMessage}`, 'success');
        }
      });
  }

  atualizarUsuario(usuario: Usuario, index?: any) {
    this.usuarioSevice.atualizarUsuario(usuario)
      .subscribe(
        {
          next: (usuarioRetorno: Usuario) => {
            this.usuariosCompletos[index] = usuarioRetorno;
            this.atualizarDadosTabela();
            this.notificacaoService.showFeedback('Atualizado com sucesso', 'success');
          }, error: (error) => {
            this.notificacaoService.showFeedback(`${error.error.errorMessage}`, 'success');
          }
        });
  }

  deletarUsuario(id: string) {
    this.usuarioSevice.deletarUsuario(id)
      .subscribe({
        next: (result) => {
          this.usuariosCompletos = this.usuariosCompletos.filter(usuario => usuario.id !== id);
          this.totalUsuarios = this.usuariosCompletos.length;
          this.atualizarDadosTabela();
          this.notificacaoService.showFeedback(`${result.message}`, 'success');
        },
        error: (error) => {
          this.notificacaoService.showFeedback('Error ao tentar excluir usuário', 'error');
        },
      });
  }

  atualizarDadosTabela(): void {
    this.dataSource.data = this.usuariosCompletos;
  }

  getPerfilClass(perfil: PerfilUsuarioEnum): string {
    switch (perfil) {
      case this.PerfilUsuario.ADMINISTRADOR: return 'perfil-admin';
      case this.PerfilUsuario.RESPONSAVEL: return 'perfil-responsavel';
      case this.PerfilUsuario.COLABORADOR: return 'perfil-colaborador';
      default: return '';
    }
  }

  temPermissao(): boolean {
    return this.perfilAtual === this.PerfilUsuario.ADMINISTRADOR;
  }
}