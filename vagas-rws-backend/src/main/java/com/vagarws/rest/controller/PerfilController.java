package com.vagarws.rest.controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vagarws.exceptions.CustomException;
import com.vagarws.rest.assembler.PerfilAssembler;
import com.vagarws.rest.service.PerfilService;
import com.vagarws.rest.swagger.IPerfilSwagger;
import com.vagarws.rest.to.request.PerfilRequest;
import com.vagarws.rest.to.response.PerfilResponse;

import jakarta.validation.Valid;

@RestController
public class PerfilController implements IPerfilSwagger {

	private PerfilService service;
	private PerfilAssembler assembler;

	public PerfilController(PerfilService service, PerfilAssembler assembler) {
		super();
		this.service = service;
		this.assembler = assembler;
	}

	@Override
	public ResponseEntity<?> create(@Valid @RequestBody PerfilRequest perfilRequest) throws CustomException {

		EntityModel<PerfilResponse> perfilResponse = this.assembler.toModel(this.service.registar(perfilRequest));
		return new ResponseEntity<>(perfilResponse, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> findById(@PathVariable String id) {
		EntityModel<PerfilResponse> perfilResponse = this.assembler.toModel(this.service.findById(id));
		return new ResponseEntity<>(perfilResponse, HttpStatus.OK);
	}

}
