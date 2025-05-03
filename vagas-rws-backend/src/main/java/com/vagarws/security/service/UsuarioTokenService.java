package com.vagarws.security.service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vagarws.exceptions.CustomException;
import com.vagarws.rest.entities.Usuario;
import com.vagarws.rest.entities.UsuarioToken;
import com.vagarws.rest.repository.UsuarioRepository;
import com.vagarws.security.repository.UsuarioTokenRepository;

/**
 * Class responsavel em gerenciar o fluxo de token da aplicação
 * 
 * @author robson.silva
 */
@Service
public class UsuarioTokenService {

	@Value("${vagarws.authenticator.token.refresh.expiration}")
	private Long tokenRefreshExpiration;

	private UsuarioTokenRepository refreshTokenRepository;

	private UsuarioRepository usuarioRepository;

	public UsuarioTokenService(UsuarioTokenRepository refreshTokenRepository, UsuarioRepository usuarioRepository) {
		super();
		this.refreshTokenRepository = refreshTokenRepository;
		this.usuarioRepository = usuarioRepository;
	}

	/**
	 * Método responsavel e registar/criar o UsuarioToken para armazenar o ID do
	 * refresh token
	 * 
	 * @author robson.silva
	 * @param idUsuario
	 * @param refreshToken
	 * @return
	 */
	public UsuarioToken createRefreshToken(String idUsuario, String refreshToken) {
		UsuarioToken usuarioToken = new UsuarioToken();
		usuarioToken.setUsuario(usuarioRepository.findById(idUsuario).get());
		usuarioToken.setDataExpiracao(LocalDateTime.now().plusDays(tokenRefreshExpiration));
		usuarioToken.setRefreshToken(refreshToken);

		usuarioToken = refreshTokenRepository.save(usuarioToken);
		return usuarioToken;
	}

	/**
	 * Método para verificar se o token está expirado
	 * 
	 * @author robson.silva
	 * @param token
	 * @return
	 */
	public UsuarioToken verificarTokenExpirado(UsuarioToken usuarioToken) {
		if (Objects.nonNull(usuarioToken) && Objects.nonNull(usuarioToken.getDataExpiracao())
				&& usuarioToken.getDataExpiracao().compareTo(LocalDateTime.now()) < 0) {
			this.refreshTokenRepository.delete(usuarioToken);
			throw new CustomException("Token foi expirado. Por favor realize o login novamente.",
					HttpStatus.UNAUTHORIZED);
		}
		return usuarioToken;
	}

	/**
	 * Método responsavel em deletar o {@link UsuarioToken} pelo ID do
	 * {@link Usuario}
	 * 
	 * @author robson.silva
	 * @param idUsuario
	 * @return
	 */
	public int deleteToken(String idUsuario) {
		return refreshTokenRepository.deleteTokenByIdUsuario(idUsuario);
	}

	/**
	 * Método para recuperar os dados do {@link UsuarioToken}
	 * 
	 * @author robson.silva
	 * @param token
	 * @return
	 */
	public Optional<UsuarioToken> findByToken(String token) {
		return refreshTokenRepository.findByRefreshToken(token);
	}

	/**
	 * Método para recuperar um {@link UsuarioToken} pelo ID {@link Usuario}
	 * 
	 * @author robson.silva
	 * @param idUsuario
	 * @return
	 */
	public Optional<UsuarioToken> getUsuarioTokenByIdUsuario(String idUsuario) {
		return this.refreshTokenRepository.findByUsuario_Id(idUsuario);
	}
}
