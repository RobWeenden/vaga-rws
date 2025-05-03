package com.vagarws.security.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vagarws.exceptions.CustomException;
import com.vagarws.rest.assembler.UsuarioAssembler;
import com.vagarws.rest.entities.ResourceOwner;
import com.vagarws.rest.entities.Usuario;
import com.vagarws.rest.entities.UsuarioToken;
import com.vagarws.rest.service.UsuarioService;
import com.vagarws.rest.to.request.UsuarioRequest;
import com.vagarws.rest.to.response.UsuarioResponse;
import com.vagarws.security.service.UsuarioTokenService;
import com.vagarws.security.swagger.IAuthSwagger;
import com.vagarws.security.to.request.LoginRequest;
import com.vagarws.security.to.response.JwtResponse;
import com.vagarws.security.util.JwtUtils;
import com.vagarws.util.ConstantsUtil;
import com.vagarws.util.ValidationUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

/**
 * Class Responsavel em realizar o controle de login do usuario
 * 
 * @author robson.silva
 */
@RestController
public class AuthController implements IAuthSwagger {

	private AuthenticationManager authenticationManager;
	private JwtUtils jwtUtils;
	private UsuarioTokenService usuarioTokenService;
	private UsuarioService service;
	private UsuarioAssembler assembler;


	public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
			UsuarioTokenService usuarioTokenService, UsuarioService service, UsuarioAssembler assembler) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
		this.usuarioTokenService = usuarioTokenService;
		this.service = service;
		this.assembler = assembler;
	}

	/**
	 * Método responsavel em realizar o login
	 * 
	 * @author robson.silva
	 * @author robson.silva
	 * @param loginRequest
	 * @return JwtResponse
	 */
	@Override
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		ResourceOwner resourceOwner = (ResourceOwner) userDetails;
		Usuario usuario = resourceOwner.getUsuario();

		ResponseCookie acessoTokenCookie = jwtUtils.gerenateTokenCookie(usuario);
		String refreshToken = jwtUtils.generateRefreshToken();
		List<String> roles = usuario.getListaPerfil().stream().map(perfil -> perfil.getAuthority()).toList();

		response.addHeader(HttpHeaders.SET_COOKIE, acessoTokenCookie.toString());
		response.addHeader(HttpHeaders.SET_COOKIE, jwtUtils.generateRefreshTokenCookie(refreshToken).toString());
		Optional<UsuarioToken> usuarioToken = this.usuarioTokenService.getUsuarioTokenByIdUsuario(usuario.getId());

		if (usuarioToken.isPresent()) {
			this.usuarioTokenService.deleteToken(usuario.getId());
		}
		this.usuarioTokenService.createRefreshToken(usuario.getId(), refreshToken);
		return ResponseEntity.ok(new JwtResponse(usuario, roles));
	}

	/**
	 * Método responsavel em realizar o refresh token
	 * 
	 * @author robson.silva
	 * @param response
	 * @param refreshToken
	 * @return
	 */
	@Override
	public ResponseEntity<?> refreshToken(HttpServletResponse response,
			@CookieValue(ConstantsUtil.REFRESH_TOKEN) String refreshToken) {

		if (refreshToken != null && refreshToken.length() > 0) {
			return usuarioTokenService.findByToken(refreshToken).map(usuarioTokenService::verificarTokenExpirado)
					.map(UsuarioToken::getUsuario).map(usuario -> {
						String tokenCookie = jwtUtils.gerenateTokenCookie(usuario).toString();
						response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie);
						return ResponseEntity.ok(new JwtResponse(usuario.getCredenciais().getEmail(), tokenCookie));
					}).orElseThrow(() -> new CustomException(ConstantsUtil.NOT_EXISTS_TOKEN, HttpStatus.UNAUTHORIZED));
		}
		return new ResponseEntity<>(ConstantsUtil.TOKEN_EMPTY, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Método responsavel em gerar novo token de acesso
	 * 
	 * @author robson.silva
	 * @author robson.silva
	 * @param loginRequest
	 * @return JwtResponse
	 */
	@Override
	public ResponseEntity<?> generateToken(@Valid @RequestBody LoginRequest loginRequest,
			HttpServletResponse response) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		ResourceOwner resourceOwner = (ResourceOwner) userDetails;
		Usuario usuario = resourceOwner.getUsuario();

		ResponseCookie acessoTokenCookie = jwtUtils.gerenateTokenCookie(usuario);
		String refreshToken = jwtUtils.generateRefreshToken();

		response.addHeader(HttpHeaders.SET_COOKIE, acessoTokenCookie.toString());
		response.addHeader(HttpHeaders.SET_COOKIE, jwtUtils.generateRefreshTokenCookie(refreshToken).toString());
		Optional<UsuarioToken> usuarioToken = this.usuarioTokenService.getUsuarioTokenByIdUsuario(usuario.getId());

		if (usuarioToken.isPresent()) {
			this.usuarioTokenService.deleteToken(usuario.getId());
		}
		this.usuarioTokenService.createRefreshToken(usuario.getId(), refreshToken);
		return ResponseEntity.ok(new JwtResponse(usuario.getCredenciais().getEmail(), acessoTokenCookie.getValue()));
	}

	@Override
	public ResponseEntity<String> logout() {
		SecurityContextHolder.clearContext();
		return new ResponseEntity<String>("Logout Successfully!", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> create(@Valid UsuarioRequest usarioRequest) {
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

}
