package com.vagarws.rest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vagarws.rest.entities.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, String> {

	Optional<Perfil> findByPerfil(String perfil);

	boolean existsByPerfil(String perfil);

}
