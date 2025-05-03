import { EtapaCandidatura } from "./etapa-candidatura";
import { Usuario } from "./usuario";
import { Vaga } from "./vaga";

export interface Candidatura {
    id?: string;
    nome?: string;
    concluida?: boolean;
    dataLimite?: Date;
    dataCandidatura?: Date;
    status?: string;
    logo?: string;
    empresa?: string;
    cargo?: string;
    feedback?: string;
    vaga?: Vaga;
    usuarioCandidato?: Usuario;
    usuarioResponsavel?: Usuario;
    etapas?: EtapaCandidatura[]
}