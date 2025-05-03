package com.vagarws.security.swagger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vagarws.rest.to.request.UsuarioRequest;
import com.vagarws.security.to.request.LoginRequest;
import com.vagarws.util.ConstantsUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Tag(description = "API para realizar autenticação e autorização", name = "Authentication")
@RequestMapping("/auth")
public interface IAuthSwagger {

	@Operation(summary = "Login", description = "Este endpoint é destinado para realizar o login do usuário.", security = @SecurityRequirement(name = ConstantsUtil.SECURITY_SWAGGER))
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response);

	@Operation(summary = "Refresh Token", description = "Este endpoint é destinado para realizar o refresh token do usuario", security = @SecurityRequirement(name = ConstantsUtil.SECURITY_SWAGGER))
	@PostMapping("/refreshToken")
	public ResponseEntity<?> refreshToken(HttpServletResponse response,
			@CookieValue("refresh_token") String refreshToken);

	@Operation(summary = "Token", description = "Este endpoint é destinado para gerar o novo token.", security = @SecurityRequirement(name = ConstantsUtil.SECURITY_SWAGGER))
	@PostMapping("/generate-token")
	public ResponseEntity<?> generateToken(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response);

	@PostMapping("/logout")
	public ResponseEntity<?> logout();
	
	@Operation(summary = "Registrar Usuário", description = "Este endpoint é destinado para cadastro de um usuario com perfil padrão.", security = @SecurityRequirement(name = ConstantsUtil.SECURITY_SWAGGER))
	@ApiResponses(value = {
			@ApiResponse(responseCode = ConstantsUtil.CREATED, description = "Recurso criado com sucesso."),
			@ApiResponse(responseCode = ConstantsUtil.NOT_FOUND, description = "Recurso não foi encontrado."),
			@ApiResponse(responseCode = ConstantsUtil.CONFLICT, description = "Já possui usuario cadastrado com o e-mail informado."),
			@ApiResponse(responseCode = ConstantsUtil.UNAUTHORIZED, description = "O cliente não possui permissão para acesso ao recurso.") })
	@PostMapping("/cadastro")
	public ResponseEntity<?> create(@Valid @RequestBody UsuarioRequest usarioRequest);

}
