package com.vagarws.rest.to.response;

import java.time.LocalDate;

import org.springframework.hateoas.server.core.Relation;

import com.vagarws.rest.entities.Usuario;
import com.vagarws.util.ConstantsUtil;

import lombok.Data;

/**
 * Class object transfer da resposta
 * 
 * @author robson.silva
 */
@Data
@Relation(collectionRelation = ConstantsUtil.COLLECTION_USUARIO)
public class UsuarioResponse {

	private String id;
	private Boolean ativo;
	private String nome;
	private LocalDate dataNascimento;
	private String cpf;
	private String email;
	private String cargo;
	private String perfil;
	private String telefone;
	private String departamento;
	private LocalDate dataCadastro;

	public UsuarioResponse() {
		super();
	}

	public UsuarioResponse(Usuario usuario) {
		super();
		this.id = usuario.getId();
		this.ativo = usuario.getAtivo();
		this.nome = usuario.getNome();
		this.dataNascimento = usuario.getDataNascimento();
		this.cpf = usuario.getCpf();
	}

}
