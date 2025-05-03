
export interface Usuario {
    id?: string;
    nome?: string;
    email?: string;
    senha?: string;
    cargo?: string;
    ativo?: boolean;
    telefone?: string;
    cpf?: string;
    confirmarSenha?: string;
    dataCadastro?: Date;
    ultimoAcesso?: Date;
    departamento?: string;
    perfil?: string;
}