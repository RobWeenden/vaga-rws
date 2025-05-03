import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map, Observable, take, throwError } from "rxjs";
import { environment } from "../../environments/environments";
import { Usuario } from "../core/models/usuario";

@Injectable({
    providedIn: 'root'
})
export class UsuarioService {
    private readonly apiUrl = `${environment.backend}/usuarios`;

    private readonly httpOptions = {
        headers: new HttpHeaders({
            'Content-Type': 'application/json'
        })
    };

    constructor(private readonly http: HttpClient) { }


    criarUsuario(usuario: Usuario): Observable<Usuario> {
        return this.http.post<Usuario>(this.apiUrl, usuario, this.httpOptions)
            .pipe(take(1));
    }

    buscarUsuarios(): Observable<any[]> {
        return this.http.get<any>(this.apiUrl, this.httpOptions)
            .pipe(take(1))
            .pipe(map(response => response._embedded?.usuarios || []));
    }

    atualizarUsuario(usuario: Usuario): Observable<any> {
        return this.http.put<any>(this.apiUrl, usuario, this.httpOptions)
            .pipe(take(1))
    }

    deletarUsuario(id: string): Observable<any> {
        return this.http.delete<any>(`${this.apiUrl + '/' + id}`, this.httpOptions).pipe(take(1));
    }

    buscarUsuariosSemPerfilColaborador(): Observable<any[]> {
        return this.http.get<any>(this.apiUrl + '/getAllUsuariosWithOutPerfilColaborador', this.httpOptions)
            .pipe(take(1))
            .pipe(map(response => response._embedded?.usuarios || []));
    }

}