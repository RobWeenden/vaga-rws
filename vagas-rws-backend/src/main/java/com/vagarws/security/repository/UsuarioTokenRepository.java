package com.vagarws.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vagarws.rest.entities.UsuarioToken;

import jakarta.transaction.Transactional;

/**
 * Class repositorio do {@link UsuarioToken}
 * 
 * @author robson.silva
 */
@Transactional
@Repository
public interface UsuarioTokenRepository extends JpaRepository<UsuarioToken, String> {

	@Modifying
	@Query("DELETE FROM UsuarioToken rt WHERE rt.usuario.id = :idUsuario")
	int deleteTokenByIdUsuario(@Param("idUsuario") String usuarioId);

	Optional<UsuarioToken> findByRefreshToken(String token);

	Optional<UsuarioToken> findByUsuario_Id(String idUsuario);

}
