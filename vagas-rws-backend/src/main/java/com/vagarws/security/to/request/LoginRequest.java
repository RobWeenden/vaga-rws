package com.vagarws.security.to.request;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.vagarws.util.ConstantsUtil;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonPropertyOrder({ "email", "senha" })
public class LoginRequest {

	@NotBlank(message = ConstantsUtil.EMAIL_OBRIGATORIO)
	private String email;

	@NotBlank(message = ConstantsUtil.SENHA_OBRIGATORIO)
	private String senha;

}
