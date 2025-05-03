package com.vagarws.rest.entities;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Class responsavel em incorporar no JPA
 * 
 * @author robson.silva
 */
@Embeddable
@NoArgsConstructor
@DynamicUpdate
public class Credenciais {

	@Getter
	private String email;
	
	@Getter
	private String senha;

	public Credenciais(String email, String senha) {
		this.email = email;
		this.senha = senha;
	}

}
