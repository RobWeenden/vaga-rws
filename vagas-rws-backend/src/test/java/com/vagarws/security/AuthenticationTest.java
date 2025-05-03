package com.vagarws.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.util.UriComponentsBuilder;

import com.vagarws.VagaRwsApplicationTests;
import com.vagarws.exceptions.CustomException;
import com.vagarws.rest.assembler.UsuarioAssembler;
import com.vagarws.rest.controller.UsuarioController;
import com.vagarws.rest.entities.ResourceOwner;
import com.vagarws.rest.entities.Usuario;
import com.vagarws.rest.entities.UsuarioToken;
import com.vagarws.rest.repository.PerfilRepository;
import com.vagarws.rest.repository.UsuarioRepository;
import com.vagarws.rest.service.UsuarioService;
import com.vagarws.security.controller.AuthController;
import com.vagarws.security.repository.UsuarioTokenRepository;
import com.vagarws.security.service.UsuarioDetailService;
import com.vagarws.security.service.UsuarioTokenService;
import com.vagarws.security.to.request.LoginRequest;
import com.vagarws.security.to.response.JwtResponse;
import com.vagarws.util.FileUtils;

import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;

/**
 * Class de teste do fluxo de autenticação e autorização
 * 
 * @author robson.silva
 */
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
public class AuthenticationTest extends VagaRwsApplicationTests {

//	<============== SERVICES MOCKS =========================>
	@Mock
	protected UsuarioDetailService usuarioDetailService;

	@Mock
	protected UsuarioTokenService usuarioTokenService;

	@InjectMocks
	protected AuthController authController;

	@Mock
	protected UsuarioService usuarioService;

	@Mock
	protected UsuarioAssembler usuarioAssembler;

	@InjectMocks
	protected UsuarioController usuarioController;
	

// <=============== REPOSITORY MOCKS =======================>
	@Autowired
	protected UsuarioTokenRepository usuarioTokenRepository;

	@Autowired
	protected UsuarioRepository usuarioRepository;

	@Autowired
	protected PerfilRepository perfilRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String URI_POST_LOGIN = UriComponentsBuilder.fromPath("/auth/login").toUriString();
	private static final String URI_POST_REFRESH_TOKEN = UriComponentsBuilder.fromPath("/auth/refreshToken")
			.toUriString();
	private static final String URI_POST_GENERATE_TOKEN = UriComponentsBuilder.fromPath("/auth/generate-token")
			.toUriString();

	@BeforeEach
	void setUp() throws IOException {
		MockitoAnnotations.openMocks(this);
		authController = new AuthController(authenticationManager, jwtUtils, usuarioTokenService, null, null);
		usuarioDetailService = new UsuarioDetailService(usuarioRepository);
	}

	@Test
	void testLoginSuccess() throws IOException {
		String sql = new String(Files.readAllBytes(Paths.get("src/test/resources/dataset.sql")));
		jdbcTemplate.execute(sql);
		
		LoginRequest loginRequest = FileUtils.fetchScript("json/security/login.json", LoginRequest.class);
		UserDetails userDetails = Mockito.mock(ResourceOwner.class);
		userDetails = usuarioDetailService.loadUserByUsername(loginRequest.getEmail());
		ResourceOwner resourceOwner = (ResourceOwner) userDetails;
		Usuario usuario = resourceOwner.getUsuario();

		ResponseCookie tokenCookie = ResponseCookie.from("token", "tokenValue").build();

		when(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha())))
				.thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(userDetails);
		when(jwtUtils.gerenateTokenCookie(usuario)).thenReturn(tokenCookie);
		when(jwtUtils.generateRefreshToken()).thenReturn(UUID.randomUUID().toString());
		when(jwtUtils.generateRefreshTokenCookie(anyString()))
				.thenReturn(ResponseCookie.from("refresh_token", "refresh_value").build());
		when(usuarioTokenService.getUsuarioTokenByIdUsuario("8b9a2bce-9003-47c0-b773-e73832dc41ba"))
				.thenReturn(Optional.empty());

		ResponseEntity<?> responseEntity = authController.login(loginRequest, response);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

		JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
		assertEquals(usuario.getCredenciais().getEmail(), jwtResponse.getEmail());
		assertNotNull(jwtResponse);
		assertNotNull(jwtResponse.getRoles());
		assertEquals("ROLE_ADMIN", jwtResponse.getRoles().get(0));
	}

	@Test
	void testLoginBadCredentials() {
		LoginRequest loginRequest = FileUtils.fetchScript("json/security/login.json", LoginRequest.class);
		loginRequest.setEmail("email@naocadastrado.com");
		MockHttpServletResponse response = new MockHttpServletResponse();
		when(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha())))
				.thenThrow(new BadCredentialsException("Bad credentials"));

		Exception exception = assertThrows(BadCredentialsException.class, () -> {
			authController.login(loginRequest, response);
		});
		assertEquals("Bad credentials", exception.getMessage());
	}

	/**
	 * Teste de Integração
	 * 
	 * @throws Exception
	 */
	@Test
	void testLoginBadCredentialsAndGlobalExceptionHandler() throws Exception {
		String usuarioRequestString = FileUtils.fetchScript("json/security/login-exception.json");

		mockMvc.perform(post(URI_POST_LOGIN).content(usuarioRequestString).header(HttpHeaders.CONTENT_TYPE,
				MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errorId").isNotEmpty()).andExpect(jsonPath("$.timestamp").isNotEmpty())
				.andExpect(jsonPath("$.httpStatusCode").value(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.errorMessage").value("Bad credentials"))
				.andExpect(jsonPath("$.requestUri").value("uri=" + URI_POST_LOGIN))
				.andExpect(jsonPath("$.errorDescription").value("Bad Request"))

		;
	}

	/**
	 * Teste de integração
	 * 
	 * @throws Exception
	 */
	@Test
	void testLoginRequestValidIsEmpty() throws Exception {
		String usuarioRequestString = FileUtils.fetchScript("json/security/login-empty.json");
		mockMvc.perform(post(URI_POST_LOGIN).content(usuarioRequestString).header(HttpHeaders.CONTENT_TYPE,
				MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errorId").isNotEmpty()).andExpect(jsonPath("$.timestamp").isNotEmpty())
				.andExpect(jsonPath("$.httpStatusCode").value(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.errorMessage").isNotEmpty())
				.andExpect(jsonPath("$.requestUri").value("uri=" + URI_POST_LOGIN))
				.andExpect(jsonPath("$.errorDescription").value("Bad Request"))

		;
	}

	@Test
	void testRefreshTokenSucess() {
		LoginRequest loginRequest = FileUtils.fetchScript("json/security/login.json", LoginRequest.class);
		UserDetails userDetails = Mockito.mock(ResourceOwner.class);
		userDetails = usuarioDetailService.loadUserByUsername(loginRequest.getEmail());
		ResourceOwner resourceOwner = (ResourceOwner) userDetails;
		Usuario usuario = resourceOwner.getUsuario();

		String refreshToken = "mockRefreshToken";
		UsuarioToken usuarioToken = mock(UsuarioToken.class);
		ResponseCookie tokenCookie = ResponseCookie.from("access_token", "mockAccessToken").build();

		when(usuarioTokenService.findByToken(refreshToken)).thenReturn(Optional.of(usuarioToken));
		when(usuarioTokenService.verificarTokenExpirado(usuarioToken)).thenReturn(usuarioToken);
		when(usuarioToken.getUsuario()).thenReturn(usuario);
		when(jwtUtils.gerenateTokenCookie(usuario)).thenReturn(tokenCookie);
		when(response.getHeader("Set-Cookie")).thenReturn(tokenCookie.toString());

		ResponseEntity<?> responseEntity = authController.refreshToken(response, refreshToken);
		JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();

		verify(response, atLeastOnce()).addHeader(eq(HttpHeaders.SET_COOKIE), anyString());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertTrue(response.getHeader(HttpHeaders.SET_COOKIE).contains(tokenCookie.toString()));
		assertEquals(tokenCookie.toString(), jwtResponse.getToken());
	}

	@Test
	void testRefreshTokenExceptionUnauthorized() {
		String refreshToken = "mockRefreshToken";

		Exception exception = assertThrows(CustomException.class, () -> {
			authController.refreshToken(response, refreshToken);
		});
		CustomException customException = (CustomException) exception;
		assertEquals("Não existe token cadastrado.", customException.getMessage());
		assertEquals(HttpStatus.UNAUTHORIZED, customException.getStatus());

	}

	@Test
	void testRefreshTokenExpiredException() throws Exception {
		String refreshToken = "mockRefreshToken";

		UsuarioTokenService service = new UsuarioTokenService(usuarioTokenRepository, usuarioRepository);
		UsuarioTokenRepository repository = mock(UsuarioTokenRepository.class);

		UsuarioToken usuarioToken = new UsuarioToken();
		usuarioToken.setRefreshToken(refreshToken);
		usuarioToken.setDataExpiracao(LocalDateTime.now().plusHours(1));

		AuthController auth = new AuthController(null, null, service, null, null);

		when(usuarioTokenService.findByToken(refreshToken)).thenReturn(Optional.of(usuarioToken));
		when(usuarioTokenService.verificarTokenExpirado(usuarioToken)).thenReturn(usuarioToken);
		doNothing().when(repository).delete(usuarioToken);

		Exception exception = assertThrows(CustomException.class, () -> {
			auth.refreshToken(response, refreshToken);
		});

		CustomException customException = (CustomException) exception;
		assertEquals(HttpStatus.UNAUTHORIZED, customException.getStatus());

	}

	@Test
	void testRefreshTokenEmpty() throws Exception {
		MvcResult result = mockMvc
				.perform(post(URI_POST_REFRESH_TOKEN).cookie(new Cookie("refresh_token", ""))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isBadRequest()).andReturn();

		String responseBody = result.getResponse().getContentAsString();
		assertNotNull(responseBody);
		assertEquals("O token está vazio.", responseBody);
	}

	@Test
	void testGenerateTokenSuccess() {
		LoginRequest loginRequest = FileUtils.fetchScript("json/security/login.json", LoginRequest.class);
		UserDetails userDetails = Mockito.mock(ResourceOwner.class);
		userDetails = usuarioDetailService.loadUserByUsername(loginRequest.getEmail());
		ResourceOwner resourceOwner = (ResourceOwner) userDetails;
		Usuario usuario = resourceOwner.getUsuario();

		ResponseCookie tokenCookie = ResponseCookie.from("token", "tokenValue").build();

		when(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha())))
				.thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(userDetails);
		when(jwtUtils.gerenateTokenCookie(usuario)).thenReturn(tokenCookie);
		when(jwtUtils.generateRefreshToken()).thenReturn(UUID.randomUUID().toString());
		when(jwtUtils.generateRefreshTokenCookie(anyString()))
				.thenReturn(ResponseCookie.from("refresh_token", "refresh_value").build());
		when(usuarioTokenService.getUsuarioTokenByIdUsuario("8b9a2bce-9003-47c0-b773-e73832dc41ba"))
				.thenReturn(Optional.empty());

		ResponseEntity<?> responseEntity = authController.generateToken(loginRequest, response);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

		JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
		assertEquals(usuario.getCredenciais().getEmail(), jwtResponse.getEmail());
		assertNotNull(jwtResponse);
		assertEquals("tokenValue", jwtResponse.getToken());
	}

	@Test
	void testGenerateTokenBadCredentials() {
		LoginRequest loginRequest = FileUtils.fetchScript("json/security/login.json", LoginRequest.class);
		loginRequest.setEmail("email@naocadastrado.com");
		MockHttpServletResponse response = new MockHttpServletResponse();
		when(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha())))
				.thenThrow(new BadCredentialsException("Bad credentials"));

		Exception exception = assertThrows(BadCredentialsException.class, () -> {
			authController.generateToken(loginRequest, response);
		});
		assertEquals("Bad credentials", exception.getMessage());
	}

	@Test
	void testGenerateTokenBadCredentialsAndGlobalExceptionHandler() throws Exception {
		String usuarioRequestString = FileUtils.fetchScript("json/security/login-exception.json");

		mockMvc.perform(post(URI_POST_GENERATE_TOKEN).content(usuarioRequestString).header(HttpHeaders.CONTENT_TYPE,
				MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errorId").isNotEmpty()).andExpect(jsonPath("$.timestamp").isNotEmpty())
				.andExpect(jsonPath("$.httpStatusCode").value(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.errorMessage").value("Bad credentials"))
				.andExpect(jsonPath("$.requestUri").value("uri=" + URI_POST_GENERATE_TOKEN))
				.andExpect(jsonPath("$.errorDescription").value("Bad Request"))

		;
	}

	@Test
	void testGenerateTokenRequestValidIsEmpty() throws Exception {
		String usuarioRequestString = FileUtils.fetchScript("json/security/login-empty.json");
		mockMvc.perform(post(URI_POST_GENERATE_TOKEN).content(usuarioRequestString).header(HttpHeaders.CONTENT_TYPE,
				MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errorId").isNotEmpty()).andExpect(jsonPath("$.timestamp").isNotEmpty())
				.andExpect(jsonPath("$.httpStatusCode").value(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.errorMessage").isNotEmpty())
				.andExpect(jsonPath("$.requestUri").value("uri=" + URI_POST_GENERATE_TOKEN))
				.andExpect(jsonPath("$.errorDescription").value("Bad Request"))

		;
	}

}
