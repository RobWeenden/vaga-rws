package com.vagarws.rest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vagarws.rest.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

	Optional<Usuario> findByCredenciais_Email(String email);

	boolean existsByCredenciais_Email(String email);

	@Query("SELECT u FROM Usuario u JOIN u.listaPerfil p WHERE p.perfil <> 'ROLE_COLABORADOR' ")
	List<Usuario> getAllUsuariosWithOutPerfilColaborador();
}
