export class LoginRequest {

    email?: string;
    senha?: string;
    lembrarMe?: boolean;

    constructor(email: string, senha: string, lembrarMe?: boolean) {
        this.email = email;
        this.senha = senha;
        this.lembrarMe = lembrarMe;
    }
}