package com.vagarws.rest.swagger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vagarws.rest.to.request.UsuarioRequest;
import com.vagarws.util.ConstantsUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Tag(description = "API para consulta de usuários", name = "Usuários")
@RequestMapping("/usuarios")
public interface IUsuarioSwagger {

	@Operation(summary = "Registrar Usuário", description = "Este endpoint é destinado para cadastro de um usuario com perfil padrão.", security = @SecurityRequirement(name = ConstantsUtil.SECURITY_SWAGGER))
	@ApiResponses(value = {
			@ApiResponse(responseCode = ConstantsUtil.CREATED, description = "Recurso criado com sucesso."),
			@ApiResponse(responseCode = ConstantsUtil.NOT_FOUND, description = "Recurso não foi encontrado."),
			@ApiResponse(responseCode = ConstantsUtil.CONFLICT, description = "Já possui usuario cadastrado com o e-mail informado."),
			@ApiResponse(responseCode = ConstantsUtil.UNAUTHORIZED, description = "O cliente não possui permissão para acesso ao recurso.") })
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody UsuarioRequest usarioRequest);

	@Operation(summary = "Buscar Usuário", description = "Este endpoint é destinado para recuperar dados de um usuario pelo ID.", security = @SecurityRequirement(name = ConstantsUtil.SECURITY_SWAGGER))
	@ApiResponses(value = {
			@ApiResponse(responseCode = ConstantsUtil.OK, description = "Retorna o recurso solicitado."),
			@ApiResponse(responseCode = ConstantsUtil.NOT_FOUND, description = "Recurso não foi encontrado."),
			@ApiResponse(responseCode = ConstantsUtil.BAD_REQUEST, description = "O corpo da solicitação está mal formado ou contém dados inválidos."),
			@ApiResponse(responseCode = ConstantsUtil.UNAUTHORIZED, description = "O cliente não possui permissão para acesso ao recurso.") })
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> findById(@NotBlank(message = "O campo não pode estar vazio.") @PathVariable String id);

	@Operation(summary = "Busca uma lista de usuários", description = "Este endpoint é destinado para recuperar uma lista de usuarios.", security = @SecurityRequirement(name = ConstantsUtil.SECURITY_SWAGGER))
	@ApiResponses(value = {
			@ApiResponse(responseCode = ConstantsUtil.OK, description = "Retorna o recurso solicitado."),
			@ApiResponse(responseCode = ConstantsUtil.UNAUTHORIZED, description = "O cliente não possui permissão para acesso ao recurso.") })
	@GetMapping
	public ResponseEntity<?> getAll();

	@Operation(summary = "Atualiza os dados do usuario", description = "Este endpoint é destinado para atualizar os dados do usuário.", security = @SecurityRequirement(name = ConstantsUtil.SECURITY_SWAGGER))
	@ApiResponses(value = {
			@ApiResponse(responseCode = ConstantsUtil.OK, description = "Retorna o recurso solicitado."),
			@ApiResponse(responseCode = ConstantsUtil.NOT_FOUND, description = "Recurso não foi encontrado."),
			@ApiResponse(responseCode = ConstantsUtil.BAD_REQUEST, description = "O corpo da solicitação está mal formado ou contém dados inválidos."),
			@ApiResponse(responseCode = ConstantsUtil.UNAUTHORIZED, description = "O cliente não possui permissão para acesso ao recurso.") })
	@PutMapping
	public ResponseEntity<?> update(@RequestBody UsuarioRequest usuarioRequest);

	@Operation(summary = "Deleta dados de um usuário", description = "Este endpoint é destinado para deletar dados de um usuário.", security = @SecurityRequirement(name = ConstantsUtil.SECURITY_SWAGGER))
	@ApiResponses(value = {
			@ApiResponse(responseCode = ConstantsUtil.OK, description = "Retorna o recurso solicitado."),
			@ApiResponse(responseCode = ConstantsUtil.NOT_FOUND, description = "Recurso não foi encontrado."),
			@ApiResponse(responseCode = ConstantsUtil.BAD_REQUEST, description = "O corpo da solicitação está mal formado ou contém dados inválidos."),
			@ApiResponse(responseCode = ConstantsUtil.UNAUTHORIZED, description = "O cliente não possui permissão para acesso ao recurso.") })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id);

	@GetMapping("/getAllUsuariosWithOutPerfilColaborador")
	public ResponseEntity<?> getAllUsuariosWithOutPerfilColaborador();

}
