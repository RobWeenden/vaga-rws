package com.vagarws.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vagarws.rest.entities.EtapaCandidatura;

@Repository
public interface EtapaCandidaturaRepository extends JpaRepository<EtapaCandidatura, String> {

}
