package com.vagarws.security.to.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.vagarws.rest.entities.Usuario;

import lombok.Getter;

@Getter
@JsonPropertyOrder({ "id", "username", "email", "roles", "message", "token" })
public class JwtResponse {

	private String id;
	private String token;
	private String email;
	private String message;
	private String username;
	private List<String> roles;

	public JwtResponse(String email, String token) {
		super();
		this.email = email;
		this.token = token;
	}

	public JwtResponse(String message) {
		super();
		this.message = message;
	}

	public JwtResponse(String id, String username, List<String> roles) {
		super();
		this.id = id;
		this.username = username;
		this.roles = roles;
	}

	public JwtResponse(Usuario usuario, List<String> roles) {
		this.id = usuario.getId();
		this.email = usuario.getCredenciais().getEmail();
		this.username = usuario.getNome();
		this.roles = roles;
	}

}
