package com.vagarws.rest.to.request;

import java.time.LocalDate;

import lombok.Data;

/**
 * Class object transfer da requisição
 * 
 * @author robson.silva
 */
@Data
public class UsuarioRequest {

	private String id;
	private String nome;
	private String email;
	private String senha;
	private String cpf;
	private String cargo;
	private Boolean ativo;
	private String telefone;
	private String departamento;
	private String perfil;
	private LocalDate dataNascimento;
	private LocalDate dataCadastro;

}
