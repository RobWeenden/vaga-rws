import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth-guard.guard';
import { NavbarComponent } from './pages/navbar/navbar.component';

export const routes: Routes = [
    {
        path: '',
        component: NavbarComponent,
        canActivate: [authGuard],
        children: [
            {
                path: '',
                redirectTo: 'dashboard',
                pathMatch: 'full'
            },
            {
                path: 'dashboard',
                loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent),
            },
            {
                path: 'candidaturas',
                loadComponent: () => import('./pages/candidatura/candidatura-lista/candidatura-lista.component').then(m => m.CandidaturaListaComponent)
            },
            {
                path: 'vagas',
                loadComponent: () => import('./pages/vaga/vaga-lista/vaga-lista.component').then(m => m.VagaListaComponent),
                canActivate: [authGuard],
                data: { roles: ['ROLE_ADMIN'] },
            },
            {
                path: 'vagas/busca',
                loadComponent: () => import('./pages/vaga/vaga-search/vaga-search.component').then(m => m.VagaSearchComponent)
            },
            {
                path: 'vagas/detalhe/:id',
                loadComponent: () => import('./pages/vaga/vaga-detalhe/vaga-detalhe.component').then(m => m.VagaDetalheComponent)
            },
            {
                path: 'usuarios',
                loadComponent: () => import('./pages/usuario/usuario-lista/usuario-lista.component').then(m => m.UsuarioListaComponent),
                canActivate: [authGuard],
                data: { roles: ['ROLE_ADMIN'] },
            },
        ]
    },
    {
        path: 'cadastro',
        loadComponent: () => import('./pages/auth/cadastro/cadastro.component').then(m => m.CadastroComponent),
    },
    {
        path: 'login',
        loadComponent: () => import('./pages/auth/login/login.component').then(m => m.LoginComponent)
    },
    {
        path: '**',
        redirectTo: 'login'
    }
];
