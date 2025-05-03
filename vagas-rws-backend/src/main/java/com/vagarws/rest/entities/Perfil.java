package com.vagarws.rest.entities;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;

import com.vagarws.rest.to.request.PerfilRequest;
import com.vagarws.util.ConstantsUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class de entidade para os perfis de usuario
 * 
 * @author robson.silva
 */
@Entity
@Table(name = ConstantsUtil.PERFIL)
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class Perfil implements GrantedAuthority {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Getter
	@Setter
	@Column(name = ConstantsUtil.ID_PERFIL)
	private String id;

	@Getter
	@Setter
	@Column(unique = true)
	private String perfil;

	@Override
	public String getAuthority() {
		return this.perfil;
	}

	public Perfil(PerfilRequest perfilRequest) {
		super();
		this.perfil = perfilRequest.getPerfil();
	}

}
