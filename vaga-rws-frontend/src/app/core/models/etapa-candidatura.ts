import { Candidatura } from "./candidatura";

export interface EtapaCandidatura {
    id?: string;
    descricao?: string;
    concluida?: boolean;
    dataLimite?: Date;
    candidatura?: Candidatura;
    etapaAtualConcluida?: boolean;
    proximaEtapa?: string;
    candidaturaId?: string;
    statusProximaEtapa?: string;
}