package com.vagarws.rest.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vagarws.exceptions.CustomException;
import com.vagarws.rest.entities.Usuario;
import com.vagarws.rest.entities.Vaga;
import com.vagarws.rest.repository.UsuarioRepository;
import com.vagarws.rest.repository.VagaRepository;
import com.vagarws.rest.to.request.VagaRequest;
import com.vagarws.rest.util.CustomMapperUtil;
import com.vagarws.util.ConstantsUtil;

import jakarta.transaction.Transactional;

/**
 * Class responsavel em realizar os servicos como beans do Spring
 * 
 * @author robson.silva
 */
@Service
public class VagaService {

	private VagaRepository repository;
	private CustomMapperUtil mapperUtil;
	private UsuarioRepository usuarioRepository;

	public VagaService(VagaRepository repository, CustomMapperUtil mapperUtil, UsuarioRepository usuarioRepository) {
		super();
		this.repository = repository;
		this.mapperUtil = mapperUtil;
		this.usuarioRepository = usuarioRepository;
	}

	public Vaga create(VagaRequest vagaRequest) {
		Vaga vaga = new Vaga();
		this.mapperUtil.updateTOEntity(vagaRequest, vaga);
		return this.repository.save(vaga);

	}

	public Vaga findById(String id) {
		return this.repository.findById(id)
				.orElseThrow(() -> new CustomException(ConstantsUtil.NOT_FOUND_MSG, HttpStatus.NOT_FOUND));
	}

	public List<Vaga> getAll() {
		return this.repository.findAll();
	}

	public void delete(String id) {
		Vaga vaga = this.repository.findById(id)
				.orElseThrow(() -> new CustomException(ConstantsUtil.NOT_FOUND_MSG, HttpStatus.NOT_FOUND));
		this.repository.delete(vaga);
	}

	public Vaga update(VagaRequest vagaRequest) {
		Vaga vaga = this.repository.findById(vagaRequest.getId())
				.orElseThrow(() -> new CustomException(ConstantsUtil.NOT_FOUND_MSG, HttpStatus.NOT_FOUND));

		if (vagaRequest.getUsuarioResponsavelID() != null) {
			Usuario usuario = this.usuarioRepository.findById(vagaRequest.getUsuarioResponsavelID())
					.orElseThrow(() -> new CustomException(ConstantsUtil.NOT_FOUND_MSG, HttpStatus.NOT_FOUND));
			vaga.setUsuarioResponsavel(usuario);
		}

		this.mapperUtil.updateTOEntity(vagaRequest, vaga);
		return this.repository.save(vaga);

	}

	public List<Vaga> findByTitulo(String titulo) {
		return repository.findByTitulo(titulo);
	}

}
