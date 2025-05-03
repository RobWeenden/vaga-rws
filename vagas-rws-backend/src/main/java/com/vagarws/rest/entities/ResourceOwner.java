package com.vagarws.rest.entities;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

/**
 * Class responsavel em representar um usuário autenticado
 * 
 * @author robson.silva
 */
@Data
public class ResourceOwner implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	public ResourceOwner(Usuario usuario) {
		super();
		this.usuario = usuario;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.usuario.getListaPerfil();
	}

	@Override
	public String getPassword() {
		return this.usuario.getCredenciais().getSenha();
	}

	@Override
	public String getUsername() {
		return this.usuario.getCredenciais().getEmail();
	}

}
