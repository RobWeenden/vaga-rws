import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map, Observable, take } from "rxjs";
import { environment } from "../../environments/environments";
import { Candidatura } from "../core/models/candidatura";
import { EtapaCandidatura } from '../core/models/etapa-candidatura';
import { Vaga } from "../core/models/vaga";

@Injectable({
    providedIn: 'root'
})
export class CandidaturaService {
    private readonly apiUrl = `${environment.backend}/candidaturas`;

    private readonly httpOptions = {
        headers: new HttpHeaders({
            'Content-Type': 'application/json'
        })
    };

    constructor(private readonly http: HttpClient) { }


    buscarCandidaturas(): Observable<any[]> {
        return this.http.get<any>(this.apiUrl, this.httpOptions)
            .pipe(take(1))
            .pipe(map(response => response._embedded?.candidaturas || []));
    }

    atualizarEtapa(etapaCandidatura: EtapaCandidatura): Observable<any> {
        return this.http.put<any>(this.apiUrl, etapaCandidatura)
            .pipe(take(1))
    }

    deletar(id: string): Observable<any> {
        return this.http.delete<any>(`${this.apiUrl + '/' + id}`, this.httpOptions).pipe(take(1));
    }

    buscarCandidaturasByIdUsuarioCandidato(idUsuarioCandidato: string): Observable<any[]> {
        return this.http.get<any>(this.apiUrl + '/buscar/candidato/' + idUsuarioCandidato, this.httpOptions).pipe(take(1))
            .pipe(map(response => response._embedded?.candidaturas || []));
    }

    buscarCandidaturasByIdUsuarioResponsavel(idUsuarioResponsavel: string): Observable<any[]> {
        return this.http.get<any>(this.apiUrl + '/buscar/responsavel/' + idUsuarioResponsavel, this.httpOptions).pipe(take(1))
            .pipe(map(response => response._embedded?.candidaturas || []));
    }

    candidatar(idVaga: string, idUsuario: any): Observable<Candidatura> {
        return this.http.post<any>(this.apiUrl + '/' + idVaga + '/' + idUsuario, this.httpOptions).pipe(take(1));
    }

}