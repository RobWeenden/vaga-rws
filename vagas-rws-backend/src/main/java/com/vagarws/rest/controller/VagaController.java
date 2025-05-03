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
import com.vagarws.rest.assembler.VagaAssembler;
import com.vagarws.rest.service.VagaService;
import com.vagarws.rest.swagger.IVagaSwagger;
import com.vagarws.rest.to.request.VagaRequest;
import com.vagarws.rest.to.response.VagaResponse;
import com.vagarws.util.ConstantsUtil;
import com.vagarws.util.ValidationUtil;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
public class VagaController implements IVagaSwagger {

	private VagaService service;
	private VagaAssembler assembler;

	public VagaController(VagaService service, VagaAssembler assembler) {
		this.service = service;
		this.assembler = assembler;
	}

	@Override
	public ResponseEntity<?> create(@Valid @RequestBody VagaRequest vagaRequest) throws CustomException {
		if (Objects.isNull(vagaRequest)) {
			throw new CustomException(ConstantsUtil.REQUISICAO_VAZIA, HttpStatus.NOT_FOUND);
		}
		EntityModel<VagaResponse> vagaResponse = this.assembler.toModel(this.service.create(vagaRequest));
		return new ResponseEntity<>(vagaResponse, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> findById(@NotBlank(message = ConstantsUtil.VALOR_EMPTY) @PathVariable String id) {
		if (ValidationUtil.isNullOrEmpty(id)) {
			throw new CustomException(ConstantsUtil.DADO_INVALIDO, HttpStatus.BAD_REQUEST);
		}
		EntityModel<VagaResponse> vagaResponse = this.assembler.toModel(this.service.findById(id));
		return new ResponseEntity<>(vagaResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAll() {
		CollectionModel<EntityModel<VagaResponse>> vagaResponse = this.assembler
				.toCollectionModel(this.service.getAll());
		return new ResponseEntity<>(vagaResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> update(@RequestBody VagaRequest vagaRequest) {
		if (Objects.isNull(vagaRequest)
				|| (Objects.nonNull(vagaRequest) && ValidationUtil.isNullOrEmpty(vagaRequest.getId()))) {
			throw new CustomException(ConstantsUtil.DADO_INVALIDO, HttpStatus.BAD_REQUEST);
		}
		EntityModel<VagaResponse> vagaResponse = this.assembler.toModel(this.service.update(vagaRequest));
		return new ResponseEntity<>(vagaResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> delete(@PathVariable String id) {
		if (ValidationUtil.isNullOrEmpty(id)) {
			throw new CustomException(ConstantsUtil.DADO_INVALIDO, HttpStatus.BAD_REQUEST);
		}
		this.service.delete(id);
		Map<String, String> response = new HashMap<>();
		response.put("message", ConstantsUtil.USUARIO_DELETADO_SUCESSO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> buscarVagas(String titulo) {
		CollectionModel<EntityModel<VagaResponse>> vagaResponse = this.assembler
				.toCollectionModel(this.service.findByTitulo(titulo));	
		return new ResponseEntity<>(vagaResponse, HttpStatus.OK);
	}

}
