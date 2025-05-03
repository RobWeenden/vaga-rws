package com.vagarws.rest.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vagarws.exceptions.CustomException;
import com.vagarws.rest.entities.Candidatura;
import com.vagarws.rest.entities.EtapaCandidatura;
import com.vagarws.rest.entities.Usuario;
import com.vagarws.rest.entities.Vaga;
import com.vagarws.rest.repository.CandidaturaRepository;
import com.vagarws.rest.repository.UsuarioRepository;
import com.vagarws.rest.repository.VagaRepository;
import com.vagarws.rest.to.enums.StatusCandidaturaEnum;
import com.vagarws.rest.to.request.CandidaturaRequest;
import com.vagarws.rest.to.request.EtapaCandidaturaRequest;
import com.vagarws.rest.util.CustomMapperUtil;
import com.vagarws.util.ConstantsUtil;

import jakarta.transaction.Transactional;

/**
 * Class responsavel em realizar os servicos como beans do Spring
 * 
 * @author robson.silva
 */
@Service
public class CandidaturaService {

	private CandidaturaRepository repository;
	private CustomMapperUtil mapperUtil;
	private UsuarioRepository usuarioRepository;
	private VagaRepository vagaRepository;

	public CandidaturaService(CandidaturaRepository repository, CustomMapperUtil mapperUtil,
			UsuarioRepository usuarioRepository, VagaRepository vagaRepository) {
		super();
		this.repository = repository;
		this.mapperUtil = mapperUtil;
		this.usuarioRepository = usuarioRepository;
		this.vagaRepository = vagaRepository;
	}

	public Candidatura create(CandidaturaRequest candidaturaRequest) {
		Candidatura candidatura = new Candidatura();
		this.mapperUtil.updateTOEntity(candidaturaRequest, candidatura);
		return this.repository.save(candidatura);

	}

	public Candidatura findById(String id) {
		return this.repository.findById(id)
				.orElseThrow(() -> new CustomException(ConstantsUtil.NOT_FOUND_MSG, HttpStatus.NOT_FOUND));
	}

	public List<Candidatura> getAll() {
		return this.repository.findAll();
	}

	public void delete(String id) {
		this.repository.deleteById(id);
	}

	public Candidatura update(EtapaCandidaturaRequest etapaCandidaturaRequest, String candidaturaId) {
		Candidatura candidatura = this.repository.findById(candidaturaId)
				.orElseThrow(() -> new CustomException(ConstantsUtil.NOT_FOUND_MSG, HttpStatus.NOT_FOUND));

		EtapaCandidatura etapa = null;
		for (EtapaCandidatura etapaLoop : candidatura.getEtapas()) {
			if (etapaLoop.getConcluida() != null && !etapaLoop.getConcluida()) {
				etapaLoop.setConcluida(etapaCandidaturaRequest.getEtapaAtualConcluida());
				break;
			}
		}

		for (EtapaCandidatura etapaLoop : candidatura.getEtapas()) {
			if (etapaLoop.getDescricao().equalsIgnoreCase(etapaCandidaturaRequest.getProximaEtapa())) {
				etapa = etapaLoop;
				break;
			}

		}

		if (etapa == null) {
			etapa = new EtapaCandidatura();
			etapa.setCandidatura(candidatura);
			etapa.setDescricao(etapaCandidaturaRequest.getProximaEtapa());
			etapa.setDataLimite(candidatura.getVaga().getDataPrazoInscricao());
			etapa.setConcluida(false);
			candidatura.setEtapas(new ArrayList<>(Arrays.asList(etapa)));
		}

		candidatura.setStatus(StatusCandidaturaEnum.fromValor(etapaCandidaturaRequest.getStatusProximaEtapa()));
		return this.repository.save(candidatura);

	}

	@Transactional
	public Candidatura candidatar(String vagaId, String usuarioId) {
		Vaga vaga = vagaRepository.findById(vagaId)
				.orElseThrow(() -> new CustomException("Vaga não encontrada", HttpStatus.NOT_FOUND));

		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new CustomException("Usuário não encontrado", HttpStatus.NOT_FOUND));

		Candidatura candidatura = new Candidatura();
		candidatura.setConcluida(false);
		candidatura.setDataCandidatura(LocalDate.now());
		candidatura.setDataLimite(vaga.getDataPrazoInscricao());
		candidatura.setEmpresa(vaga.getEmpresa());
		candidatura.setLogo(vaga.getLogoEmpresa());
		candidatura.setStatus("Triagem");
		candidatura.setUsuarioCandidato(usuario);
		candidatura.setUsuarioResponsavel(vaga.getUsuarioResponsavel());
		candidatura.setLogo("assets/logos/techcorp.png");
		candidatura.setVaga(vaga);

		EtapaCandidatura etapa = new EtapaCandidatura();
		etapa.setCandidatura(candidatura);
		etapa.setDescricao("Triagem inicial");
		etapa.setDataLimite(vaga.getDataPrazoInscricao());
		etapa.setConcluida(false);
		candidatura.setEtapas(new ArrayList<>(Arrays.asList(etapa)));

		return this.repository.save(candidatura);
	}

	public List<Candidatura> findByIdCanidaturaUsuarioCandidato(String id) {
		return this.repository.findCandidaturaByIdUsuarioCandidato(id);
	}

	public List<Candidatura> findByIdCanidaturaUsuarioResponsavel(String id) {
		return this.repository.findCandidaturaByIdUsuarioResponsavel(id);
	}

}
