import { Injectable } from "@angular/core";
import { environment } from "../../environments/environments";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Usuario } from "../core/models/usuario";
import { catchError, map, Observable, take, throwError } from "rxjs";
import { Vaga } from "../core/models/vaga";

@Injectable({
    providedIn: 'root'
})
export class VagaService {
    private readonly apiUrl = `${environment.backend}/vagas`;

    private readonly httpOptions = {
        headers: new HttpHeaders({
            'Content-Type': 'application/json'
        })
    };

    constructor(private readonly http: HttpClient) { }


    cadastrarVaga(vaga: Vaga): Observable<Vaga> {
        return this.http.post<Vaga>(this.apiUrl, vaga, this.httpOptions).pipe(take(1));
    }
    
    buscarVagas(): Observable<any[]> {
        return this.http.get<any>(this.apiUrl, this.httpOptions)
            .pipe(take(1))
            .pipe(map(response => response._embedded?.vagas || []));
    }

    atualizarVaga(vaga: Vaga): Observable<any> {
        return this.http.put<any>(this.apiUrl, vaga, this.httpOptions)
        .pipe(take(1))
    }

    deletarVaga(id: string): Observable<any> {
        return this.http.delete<any>(`${this.apiUrl + '/'+ id}`, this.httpOptions).pipe(take(1));
    }

    buscarVagasByTitulo(titulo:string): Observable<any[]> {
        let params = new HttpParams();
        if (titulo) {
          params = params.set('titulo', titulo);
        }
        return this.http.get<any>(this.apiUrl + '/buscar-vagas', {params})
            .pipe(take(1))
            .pipe(map(response => response._embedded?.vagas || []));
    }

    buscarVagasByID(idVaga:string): Observable<Vaga> {
        return this.http.get<any>(this.apiUrl + '/' + idVaga, this.httpOptions)
            .pipe(take(1));
    }
}