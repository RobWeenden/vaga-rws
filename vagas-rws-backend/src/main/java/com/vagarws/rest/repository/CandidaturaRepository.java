package com.vagarws.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vagarws.rest.entities.Candidatura;


@Repository
public interface CandidaturaRepository extends JpaRepository<Candidatura, String> {

	@Query("SELECT c FROM  Candidatura c WHERE c.usuarioCandidato.id = ?1")
	List<Candidatura> findCandidaturaByIdUsuarioCandidato(String idUsuarioCandidato);

	@Query("SELECT c FROM  Candidatura c WHERE c.usuarioResponsavel.id = ?1")
	List<Candidatura> findCandidaturaByIdUsuarioResponsavel(String idUsuarioResponsavel);

}
