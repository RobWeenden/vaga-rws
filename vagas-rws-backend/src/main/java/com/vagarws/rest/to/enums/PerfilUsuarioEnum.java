package com.vagarws.rest.to.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.vagarws.util.ConstantsUtil;
import com.vagarws.util.ValidationUtil;

public enum PerfilUsuarioEnum {
	ADMINISTRADOR("Administrador", "ROLE_ADMIN"), RESPONSAVEL("Responsável", "ROLE_RESPONSAVEL"),
	COLABORADOR("Colaborador", "ROLE_COLABORADOR");

	private final String valor;
	private final String role;

	PerfilUsuarioEnum(String valor, String role) {
		this.valor = valor;
		this.role = role;
	}

	@JsonValue
	public String getValor() {
		return valor;
	}

	public String getRole() {
		return role;
	}

	public static String fromValor(String valor) {
		for (PerfilUsuarioEnum perfil : PerfilUsuarioEnum.values()) {
			if (!ValidationUtil.isNullOrEmpty(valor) && perfil.getRole().equals(valor)) {
				return perfil.getValor();
			}
		}
		return ConstantsUtil.EMPTY;
	}

	public static String fromRole(String role) {
		for (PerfilUsuarioEnum perfil : PerfilUsuarioEnum.values()) {
			if (perfil.getValor().equals(role)) {
				return perfil.getRole();
			}
		}
		return ConstantsUtil.EMPTY;
	}
}
