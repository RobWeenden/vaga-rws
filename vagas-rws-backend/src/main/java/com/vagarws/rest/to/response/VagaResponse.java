package com.vagarws.rest.to.response;

import java.time.LocalDate;
import java.util.List;

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
@Relation(collectionRelation = ConstantsUtil.COLLECTION_VAGA)
public class VagaResponse {

	private String id;
	private String titulo;
	private String empresa;
	private String localidade;
	private String regime;
	private String modalidade;
	private String faixaSalarial;
	private String descricao;
	private List<String> requisitos;
	private List<String> beneficios;
	private List<String> diferenciais;
	private String sobreEmpresa;
	private String logoEmpresa;
	private String emailContato;
	private String status;
	private Long qtdCandidaturas;
	private LocalDate dataPublicacao;
	private LocalDate dataPrazoInscricao;
	private Usuario usuarioResponsavel;
	private Boolean isCandidato;
	private List<CandidaturaResponse> listaCandidatura;

	public VagaResponse() {
		super();
	}

}
