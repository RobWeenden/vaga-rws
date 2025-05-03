package com.vagarws.rest.to.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EtapaCandidaturaRequest {

	private String id;
	private String descricao;
	private Boolean concluida;
	private LocalDate dataLimite;
	private CandidaturaRequest candidatura;
	private Boolean etapaAtualConcluida;
	private String proximaEtapa;
	private String candidaturaId;
	private String statusProximaEtapa;
}
