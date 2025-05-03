package com.vagarws.rest.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vagarws.exceptions.CustomException;
import com.vagarws.rest.assembler.CandidaturaAssembler;
import com.vagarws.rest.service.CandidaturaService;
import com.vagarws.rest.swagger.ICandidaturaSwagger;
import com.vagarws.rest.to.request.CandidaturaRequest;
import com.vagarws.rest.to.request.EtapaCandidaturaRequest;
import com.vagarws.rest.to.response.CandidaturaResponse;
import com.vagarws.util.ConstantsUtil;
import com.vagarws.util.ValidationUtil;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
public class CandidaturaController implements ICandidaturaSwagger {

	private CandidaturaService service;
	private CandidaturaAssembler assembler;

	public CandidaturaController(CandidaturaService service, CandidaturaAssembler assembler) {
		this.service = service;
		this.assembler = assembler;
	}

	@Override
	public ResponseEntity<?> create(@Valid @RequestBody CandidaturaRequest candidaturaRequest) throws CustomException {
		if (Objects.isNull(candidaturaRequest)) {
			throw new CustomException(ConstantsUtil.REQUISICAO_VAZIA, HttpStatus.NOT_FOUND);
		}
		EntityModel<CandidaturaResponse> candidaturaResponse = this.assembler
				.toModel(this.service.create(candidaturaRequest));
		return new ResponseEntity<>(candidaturaResponse, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> findById(@NotBlank(message = ConstantsUtil.VALOR_EMPTY) @PathVariable String id) {
		if (ValidationUtil.isNullOrEmpty(id)) {
			throw new CustomException(ConstantsUtil.DADO_INVALIDO, HttpStatus.BAD_REQUEST);
		}
		EntityModel<CandidaturaResponse> candidaturaResponse = this.assembler.toModel(this.service.findById(id));
		return new ResponseEntity<>(candidaturaResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAll() {
		CollectionModel<EntityModel<CandidaturaResponse>> candidaturaResponse = this.assembler
				.toCollectionModel(this.service.getAll());
		return new ResponseEntity<>(candidaturaResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> update(EtapaCandidaturaRequest etapaCandidaturaRequest) {
		if (Objects.isNull(etapaCandidaturaRequest) || (Objects.nonNull(etapaCandidaturaRequest)
				&& ValidationUtil.isNullOrEmpty(etapaCandidaturaRequest.getCandidaturaId()))) {
			throw new CustomException(ConstantsUtil.DADO_INVALIDO, HttpStatus.BAD_REQUEST);
		}
		EntityModel<CandidaturaResponse> candidaturaResponse = this.assembler
				.toModel(this.service.update(etapaCandidaturaRequest, etapaCandidaturaRequest.getCandidaturaId()));
		return new ResponseEntity<>(candidaturaResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> cancelar(@PathVariable String id) {
		if (ValidationUtil.isNullOrEmpty(id)) {
			throw new CustomException(ConstantsUtil.DADO_INVALIDO, HttpStatus.BAD_REQUEST);
		}
		this.service.delete(id);
		Map<String, String> response = new HashMap<>();
		response.put("message", ConstantsUtil.CANDIDATURA_CANCELADA);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> candidatar(String idVaga, String idUsuario) {
		EntityModel<CandidaturaResponse> candidaturaResponse = this.assembler
				.toModel(this.service.candidatar(idVaga, idUsuario));
		return new ResponseEntity<>(candidaturaResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> findByIdCanidaturaUsuarioCandidato(
			@NotBlank(message = "O campo não pode estar vazio.") String id) {
		CollectionModel<EntityModel<CandidaturaResponse>> candidaturaResponse = this.assembler
				.toCollectionModel(this.service.findByIdCanidaturaUsuarioCandidato(id));
		return new ResponseEntity<>(candidaturaResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> findByIdCanidaturaUsuarioResponsavel(
			@NotBlank(message = "O campo não pode estar vazio.") String id) {
		CollectionModel<EntityModel<CandidaturaResponse>> candidaturaResponse = this.assembler
				.toCollectionModel(this.service.findByIdCanidaturaUsuarioResponsavel(id));
		return new ResponseEntity<>(candidaturaResponse, HttpStatus.OK);
	}

}
