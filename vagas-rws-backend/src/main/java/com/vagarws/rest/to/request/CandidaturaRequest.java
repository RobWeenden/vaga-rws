package com.vagarws.rest.to.request;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class CandidaturaRequest {

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
	private VagaRequest vaga;
	private UsuarioRequest usuarioCandidato;
	private UsuarioRequest usuarioResponsavel;
	private List<EtapaCandidaturaRequest> etapas;
}
