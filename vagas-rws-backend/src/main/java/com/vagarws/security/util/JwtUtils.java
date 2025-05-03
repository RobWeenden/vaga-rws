package com.vagarws.security.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.vagarws.rest.entities.Usuario;
import com.vagarws.util.ConstantsUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * Class util responsavel em gerenciar os tokens
 * 
 * @author robson.silva
 */
@Component
public class JwtUtils {

	public static final Duration DURATION_MAX = Duration.ofSeconds(24 * 60 * 60);

	@Value("${vagarws.authenticator.token.secret}")
	private String secret;

	@Value("${vagarws.authenticator.token.expiration}")
	private Long expiration;

	@Value("${vagarws.authenticator.token.cookie-name}")
	private String accessTokenCookieName;

	@Value("${vagarws.authenticator.token.cookie-refresh-token}")
	private String refreshTokenCookieName;

	@Value("${vagarws.authenticator.token.refresh.expiration}")
	private Long tokenRefreshExpiration;

	@Value("${server.servlet.context-path}")
	private String path;

	@Value("${vagarws.env}")
	private String env;

	/**
	 * Método para gerar o token
	 * 
	 * @author robson.silva
	 * @param userDetails
	 * @return
	 */
	public String generateToken(Usuario usuario) {
		Algorithm algorithm = Algorithm.HMAC256(secret);
		String roles = usuario.getListaPerfil().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(ConstantsUtil.JOIN_VIRGULA));

		return JWT.create().withIssuer(ConstantsUtil.ISSUER).withSubject(usuario.getCredenciais().getEmail())
				.withClaim(ConstantsUtil.USERNAME, usuario.getNome()).withClaim(ConstantsUtil.PERMISSIONS, roles)
				.withClaim(ConstantsUtil.USER_ID, usuario.getId()).withExpiresAt(generateExpirationDate())
				.sign(algorithm);

	}

	/**
	 * Método para extrair o username do token
	 * 
	 * @author robson.silva
	 * @param token
	 * @return
	 */
	public String extrairUsername(String token) {
		return JWT.decode(token).getClaim(ConstantsUtil.CLAIM_SUB).asString();
	}

	/**
	 * Método para validar o token
	 * 
	 * @author robson.silva
	 * @param token
	 * @param username
	 * @return
	 */
	public String validarToken(String token) {
		Algorithm algorithm = Algorithm.HMAC256(secret);
		return JWT.require(algorithm).withIssuer(ConstantsUtil.ISSUER).build().verify(token).getSubject();
	}

	/**
	 * Método para recuperar as claims do token
	 * 
	 * @author robson.silva
	 * @param token
	 * @return
	 */
	public Claims getClaimsFromToken(String token) {
		Algorithm algorithm = Algorithm.HMAC256(secret);
		return Jwts.parserBuilder().setSigningKey(algorithm.getSigningKeyId().getBytes()).build().parseClaimsJws(token)
				.getBody();
	}

	/**
	 * Método para recuperar as Roles do token
	 * 
	 * @author robson.silva
	 * @param token
	 * @return
	 */
	public String getRolesFromToken(String token) {
		return getClaimsFromToken(token).get(ConstantsUtil.PERMISSIONS, String.class);
	}

	/**
	 * Metodo que gera a data de expirar o token
	 * 
	 * @author robson.silva
	 * @return
	 */
	private Instant generateExpirationDate() {
		return LocalDateTime.now().plus(Duration.ofDays(this.expiration)).toInstant(ZoneOffset.of("-3"));
	}

	/**
	 * Método responsavel em gerar o token e fornece ele para o cookie
	 * 
	 * @author robson.silva
	 * @param usuario
	 * @return
	 */
	public ResponseCookie gerenateTokenCookie(Usuario usuario) {
		String jwt = this.generateToken(usuario);
		return generateCookie(accessTokenCookieName, jwt, this.path);
	}

	/**
	 * Método responsavel em gerar o refresh token e fonece ele para o cookie
	 * 
	 * @author robson.silva
	 * @param refreshToken
	 * @return
	 */
	public ResponseCookie generateRefreshTokenCookie(String refreshToken) {
		return generateCookie(refreshTokenCookieName, refreshToken, this.path);
	}

	/**
	 * Método generico para gerar cookie
	 * 
	 * @author robson.silva
	 * @param name
	 * @param token
	 * @param path
	 * @return
	 */
	private ResponseCookie generateCookie(String name, String token, String path) {
		return ResponseCookie.from(name, token).httpOnly(true).path(path).secure(!isLocalEnviroment())
				.maxAge(DURATION_MAX).sameSite(ConstantsUtil.STRICT).build();
	}

	/**
	 * Método para gerar o ID do refresh token
	 * 
	 * @author robson.silva
	 * @return
	 */
	public String generateRefreshToken() {
		return UUID.randomUUID().toString();
	}

	public ResponseCookie cleanCookieAccessToken() {
		return ResponseCookie.from(accessTokenCookieName, "").path(this.path).maxAge(0).httpOnly(true)
				.secure(!isLocalEnviroment()).sameSite(ConstantsUtil.STRICT).build();
	}

	public ResponseCookie cleanCookieRefreshToken() {
		return ResponseCookie.from(refreshTokenCookieName, "").path(this.path).maxAge(0).httpOnly(true)
				.secure(!isLocalEnviroment()).sameSite(ConstantsUtil.STRICT).build();
	}

	private boolean isLocalEnviroment() {
		return ConstantsUtil.LOCAL_HOST.equalsIgnoreCase(env);
	}

}
