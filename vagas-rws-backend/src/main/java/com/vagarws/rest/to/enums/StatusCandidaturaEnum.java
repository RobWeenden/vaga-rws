package com.vagarws.rest.to.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.vagarws.util.ConstantsUtil;

public enum StatusCandidaturaEnum {
	TRIAGEM("triagem"), VISUALIZADA("visualizada"), ENTREVISTA("entrevista"), PENDENTE("pendente"),
	APROVADA("aprovada"), REPROVADA("reprovada");

	private final String valor;

	StatusCandidaturaEnum(String valor) {
		this.valor = valor;
	}

	@JsonValue
	public String getValor() {
		return valor;
	}

	public static String fromValor(String valor) {
		for (StatusCandidaturaEnum status : StatusCandidaturaEnum.values()) {
			if (status.getValor().equals(valor)) {
				return status.getValor();
			}
		}
		return ConstantsUtil.EMPTY;
	}
}
