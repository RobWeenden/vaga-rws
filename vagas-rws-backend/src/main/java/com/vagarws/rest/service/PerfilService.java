package com.vagarws.rest.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vagarws.exceptions.CustomException;
import com.vagarws.rest.entities.Perfil;
import com.vagarws.rest.repository.PerfilRepository;
import com.vagarws.rest.to.request.PerfilRequest;
import com.vagarws.util.ConstantsUtil;

/**
 * Class responsavel em realizar os servicos como beans do Spring
 * 
 * @author robson.silva
 */
@Service
public class PerfilService {

	private PerfilRepository repository;

	public PerfilService(PerfilRepository perfilRepository) {
		super();
		this.repository = perfilRepository;
	}

	public Perfil registar(PerfilRequest perfilRequest) {
		return this.repository.save(new Perfil(perfilRequest));

	}

	public Perfil findByPerfil(String perfil, String msgException, HttpStatus httpStatus) {
		return this.repository.findByPerfil(perfil).orElseThrow(() -> new CustomException(msgException, httpStatus));
	}

	public Perfil findById(String id) {
		return this.repository.findById(id)
				.orElseThrow(() -> new CustomException(ConstantsUtil.NOT_FOUND_MSG, HttpStatus.NOT_FOUND));
	}

}
