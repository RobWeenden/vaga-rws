package com.vagarws.rest.to.response;

import java.time.LocalDate;
import java.util.List;

import org.springframework.hateoas.server.core.Relation;

import com.vagarws.util.ConstantsUtil;

import lombok.Data;

/**
 * Class object transfer da resposta
 * 
 * @author robson.silva
 */
@Data
@Relation(collectionRelation = ConstantsUtil.COLLECTION_CANDIDATURA)
public class CandidaturaResponse {

	private String id;
	private String nome;
	private Boolean concluida;
	private LocalDate dataLimite;
	private LocalDate dataCandidatura;
	private String status;
	private String logo;
	private String empresa;
	private String cargo;
	private String feedback;
	private UsuarioResponse usuarioCandidato;
	private UsuarioResponse usuarioResponsavel;
	private List<EtapaCandidaturaResponse> etapas;

	public CandidaturaResponse() {
		super();
	}

}
