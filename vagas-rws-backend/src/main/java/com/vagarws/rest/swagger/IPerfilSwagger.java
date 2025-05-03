package com.vagarws.rest.swagger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vagarws.rest.to.request.PerfilRequest;
import com.vagarws.util.ConstantsUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(description = "API para consulta de perfies", name = "Perfil")
@RequestMapping(value = "/perfil")
public interface IPerfilSwagger {

	@Operation(summary = "Registrar Perfil", description = "Este endpoint é destinado para cadastro de um perfil.", security = @SecurityRequirement(name = ConstantsUtil.SECURITY_SWAGGER))
	@ApiResponses(value = {
			@ApiResponse(responseCode = ConstantsUtil.CREATED, description = "Recurso criado com sucesso."),
			@ApiResponse(responseCode = ConstantsUtil.NOT_FOUND, description = "Recurso não foi encontrado."),
			@ApiResponse(responseCode = ConstantsUtil.CONFLICT, description = "Já possui usuario cadastrado com o e-mail informado."),
			@ApiResponse(responseCode = ConstantsUtil.UNAUTHORIZED, description = "O cliente não possui permissão para acesso ao recurso.") })
	@PostMapping
	ResponseEntity<?> create(@Valid @RequestBody PerfilRequest perfilRequest);

	@Operation(summary = "Buscar Usuário", description = "Este endpoint é destinado para recuperar dados de um perfil pelo ID.", security = @SecurityRequirement(name = ConstantsUtil.SECURITY_SWAGGER))
	@ApiResponses(value = {
			@ApiResponse(responseCode = ConstantsUtil.OK, description = "Retorna o recurso solicitado."),
			@ApiResponse(responseCode = ConstantsUtil.NOT_FOUND, description = "Recurso não foi encontrado."),
			@ApiResponse(responseCode = ConstantsUtil.BAD_REQUEST, description = "O corpo da solicitação está mal formado ou contém dados inválidos."),
			@ApiResponse(responseCode = ConstantsUtil.UNAUTHORIZED, description = "O cliente não possui permissão para acesso ao recurso.") })
	@GetMapping("/{id}")
	ResponseEntity<?> findById(@PathVariable String id);

}
