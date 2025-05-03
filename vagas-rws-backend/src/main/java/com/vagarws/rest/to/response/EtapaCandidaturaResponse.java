package com.vagarws.rest.to.response;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EtapaCandidaturaResponse {

	private String id;
	private String descricao;
	private Boolean concluida;
	private LocalDate dataLimite;
}
