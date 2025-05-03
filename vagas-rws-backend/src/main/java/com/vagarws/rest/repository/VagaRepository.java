package com.vagarws.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vagarws.rest.entities.Vaga;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, String> {

	@Query("SELECT v FROM Vaga v WHERE LOWER(v.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")
	List<Vaga> findByTitulo(@Param("titulo") String titulo);

}
