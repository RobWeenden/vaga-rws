
import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MaterialModule } from '../../../../shared/material.module';
import { ConfirmationDialogService } from '../../../components/confirmation-dialog/confirmation-dialog.service';
import { NotificationService } from '../../../components/notification/notification.service';
import { Vaga } from '../../../core/models/vaga';
import { VagaService } from '../../../services/vaga.service';
import { VagaRegistraComponent } from '../vaga-registra/vaga-registra.component';

@Component({
  selector: 'app-vaga-lista',
  imports: [
    CommonModule,
    MaterialModule
  ],
  templateUrl: './vaga-lista.component.html',
  styleUrl: './vaga-lista.component.scss'
})
export class VagaListaComponent implements OnInit {
  displayedColumns: string[] = ['titulo', 'localidade', 'modalidade', 'dataPublicacao', 'status', 'candidaturas', 'acoes'];
  dataSource = new MatTableDataSource<any>([]);
  vagasCompletos: Vaga[] = [];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  pageSizeOptions: number[] = [5, 10, 25, 50];
  pageSize = 5;
  pageIndex = 0;
  totalVagas = 0;
  filterValue: string = '';

  constructor(
    private readonly dialog: MatDialog,
    private readonly confirmationService: ConfirmationDialogService,
    private readonly vagaService: VagaService,
    private readonly notificacaoService: NotificationService
  ) { }

  ngOnInit(): void {
    this.buscarVagas();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    this.dataSource.filterPredicate = (data: any, filter: string) => {
      const searchTerm = filter.toLowerCase();
      return data.titulo.toLowerCase().includes(searchTerm) ||
        data.empresa.toLowerCase().includes(searchTerm) ||
        data.localidade.toLowerCase().includes(searchTerm);
    };
  }

  abrirDialogVaga(vaga?: Vaga): void {
    const dialogRef = this.dialog.open(VagaRegistraComponent, {
      width: '900px',
      height: '700px',
      data: vaga || {}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (result.id) {
          const index = this.vagasCompletos.findIndex(u => u.id === result.id);
          if (index !== -1) {
            this.atualizarVaga(result, index);
          }
        } else {
          this.cadastrarVaga(result)
        }
      }
    });
  }

  atualizarDadosTabela(): void {
    this.dataSource.data = this.vagasCompletos;
  }


  excluirVaga(id: string, titulo: string): void {
    this.confirmationService.confirmDelete('Vaga', `Tem certeza que deseja excluir a vaga "${titulo}"? Esta ação não pode ser desfeita.`)
      .subscribe(confirmado => {
        if (confirmado) {
          this.deletarVaga(id);
        }
      });

  }

  pausarReativarVaga(vaga: Vaga): void {
    const index = this.dataSource.data.findIndex(v => v.id === vaga.id);
    if (index !== -1) {
      const novoStatus = vaga.status === 'Ativa' ? 'Pausada' : 'Ativa';
      vaga = { ...vaga, status: novoStatus }
      this.atualizarVaga(vaga, index);
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'Ativa': return 'vaga-ativa';
      case 'Pausada': return 'vaga-pausada';
      case 'Encerrada': return 'vaga-encerrada';
      default: return '';
    }
  }

  mudarPagina(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
  }

  ordenar(event: Sort): void {
    const data = this.vagasCompletos.slice();
    if (!event.active || event.direction === '') {
      this.dataSource.data = data;
      return;
    }

    this.dataSource.data = data.sort((a, b) => {
      const isAsc = event.direction === 'asc';
      switch (event.active) {
        case 'titulo': return this.comparar(a.titulo, b.titulo, isAsc);
        case 'empresa': return this.comparar(a.empresa, b.empresa, isAsc);
        case 'localidade': return this.comparar(a.localidade, b.localidade, isAsc);
        case 'status': return this.comparar(a.status, b.status, isAsc);
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

  buscarVagas(): void {
    this.vagaService.buscarVagas()
      .subscribe((vagas: Vaga[]) => {
        if (vagas) {
          this.vagasCompletos = vagas;
          this.totalVagas = this.vagasCompletos.length;
          this.dataSource.data = this.vagasCompletos;
        }
      });
  }


  cadastrarVaga(vaga: Vaga) {
    this.vagaService.cadastrarVaga(vaga)
      .subscribe((vaga: Vaga) => {
        this.vagasCompletos.push(vaga);
        this.totalVagas = this.vagasCompletos.length;
        this.atualizarDadosTabela();
        this.notificacaoService.showFeedback('Cadastrado com sucesso', 'success');
      });
  }

  atualizarVaga(vaga: Vaga, index?: any) {
    this.vagaService.atualizarVaga(vaga)
      .subscribe((vagaRetorno: Vaga) => {

        this.vagasCompletos[index] = vagaRetorno;
        this.atualizarDadosTabela();
        this.notificacaoService.showFeedback('Atualizado com sucesso', 'success');
      });
  }


  deletarVaga(id: string) {
    this.vagaService.deletarVaga(id)
      .subscribe({
        next: (result) => {
          this.vagasCompletos = this.vagasCompletos.filter(vaga => vaga.id !== id);
          this.totalVagas = this.vagasCompletos.length;
          this.atualizarDadosTabela();
          this.notificacaoService.showFeedback(`${result.message}`, 'success');
        },
        error: (error) => {
          this.notificacaoService.showFeedback('Error ao tentar excluir usuário', 'error');
        },
      });
  }


}
