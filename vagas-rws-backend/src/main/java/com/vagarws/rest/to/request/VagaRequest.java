package com.vagarws.rest.to.request;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class VagaRequest {

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
	private UsuarioRequest usuarioResponsavel;
	private String usuarioResponsavelID;
}
