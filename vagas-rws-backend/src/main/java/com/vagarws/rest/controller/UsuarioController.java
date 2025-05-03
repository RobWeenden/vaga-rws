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
import com.vagarws.rest.assembler.UsuarioAssembler;
import com.vagarws.rest.service.UsuarioService;
import com.vagarws.rest.swagger.IUsuarioSwagger;
import com.vagarws.rest.to.request.UsuarioRequest;
import com.vagarws.rest.to.response.UsuarioResponse;
import com.vagarws.util.ConstantsUtil;
import com.vagarws.util.ValidationUtil;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
public class UsuarioController implements IUsuarioSwagger {

	private UsuarioService service;
	private UsuarioAssembler assembler;

	public UsuarioController(UsuarioService service, UsuarioAssembler assembler) {
		this.service = service;
		this.assembler = assembler;
	}

	@Override
	public ResponseEntity<?> create(@Valid @RequestBody UsuarioRequest usarioRequest) throws CustomException {
		if (Objects.isNull(usarioRequest)) {
			throw new CustomException(ConstantsUtil.REQUISICAO_VAZIA, HttpStatus.NOT_FOUND);
		}
		if (ValidationUtil.isNullOrEmpty(usarioRequest.getEmail())
				|| ValidationUtil.isNullOrEmpty(usarioRequest.getSenha())) {
			throw new CustomException(ConstantsUtil.EMAIL_SENHA_OBRIGATORIOS, HttpStatus.NOT_FOUND);
		}
		EntityModel<UsuarioResponse> usuarioResponse = this.assembler.toModel(this.service.create(usarioRequest));
		return new ResponseEntity<>(usuarioResponse, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> findById(@NotBlank(message = ConstantsUtil.VALOR_EMPTY) @PathVariable String id) {
		if (ValidationUtil.isNullOrEmpty(id)) {
			throw new CustomException(ConstantsUtil.DADO_INVALIDO, HttpStatus.BAD_REQUEST);
		}
		EntityModel<UsuarioResponse> usuarioResponse = this.assembler.toModel(this.service.findById(id));
		return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAll() {
		CollectionModel<EntityModel<UsuarioResponse>> usuarioResponse = this.assembler
				.toCollectionModel(this.service.getAll());
		return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> update(@RequestBody UsuarioRequest usuarioRequest) {
		if (Objects.isNull(usuarioRequest)
				|| (Objects.nonNull(usuarioRequest) && ValidationUtil.isNullOrEmpty(usuarioRequest.getId()))) {
			throw new CustomException(ConstantsUtil.DADO_INVALIDO, HttpStatus.BAD_REQUEST);
		}
		EntityModel<UsuarioResponse> usuarioResponse = this.assembler.toModel(this.service.update(usuarioRequest));
		return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
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
	public ResponseEntity<?> getAllUsuariosWithOutPerfilColaborador() {
		CollectionModel<EntityModel<UsuarioResponse>> usuarioResponse = this.assembler
				.toCollectionModel(this.service.getAllUsuariosWithOutPerfilColaborador());
		return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
	}

}
