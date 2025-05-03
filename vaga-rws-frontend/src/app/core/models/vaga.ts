import { Candidatura } from "./candidatura";
import { Usuario } from "./usuario";

export interface Vaga {
    id?: string;
    titulo?: string;
    empresa?: string;
    localidade?: string;
    regime?: string;
    modalidade?: string;
    faixaSalarial?: string;
    descricao?: string;
    requisitos?: string[];
    beneficios?: string[];
    diferenciais?: string[];
    sobreEmpresa?: string;
    dataPublicacao?: Date;
    status: string;
    logoEmpresa: string;
    dataPrazoInscricao: Date;
    emailContato: string;
    qtdCandidaturas?: number;
    usuarioResponsavel?: Usuario;
    isCandidato?: boolean;
    listaCandidatura?: Candidatura[];

}