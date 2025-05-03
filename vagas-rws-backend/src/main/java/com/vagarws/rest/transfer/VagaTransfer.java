package com.vagarws.rest.transfer;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;

import com.vagarws.rest.entities.Candidatura;
import com.vagarws.rest.entities.Usuario;
import com.vagarws.rest.entities.Vaga;
import com.vagarws.rest.to.request.VagaRequest;
import com.vagarws.rest.to.response.CandidaturaResponse;
import com.vagarws.rest.to.response.VagaResponse;

/**
 * Class responsavel em transferir os dados entre os objetos
 * 
 * @author robson.silva
 */
public class VagaTransfer {

	public static VagaResponse getVagaResponse(Vaga vaga) {
		VagaResponse vagaResponse = new VagaResponse();
		BeanUtils.copyProperties(vaga, vagaResponse);
		vagaResponse.setUsuarioResponsavel(new Usuario(vaga.getUsuarioResponsavel()));
		vagaResponse.setQtdCandidaturas((long) vaga.getListaCandidatura().size());

		List<CandidaturaResponse> listaCandidaturaResponses = new ArrayList<>();
		for (Candidatura candidaturaLoop : vaga.getListaCandidatura()) {
			CandidaturaResponse candidaturaResponse = CandidaturaTransfer.getCandidaturaResponse(candidaturaLoop);
			candidaturaResponse
					.setUsuarioCandidato(UsuarioTransfer.getUsuarioResponseMin(candidaturaLoop.getUsuarioCandidato()));
			listaCandidaturaResponses.add(candidaturaResponse);
		}
		vagaResponse.setListaCandidatura(listaCandidaturaResponses);
		return vagaResponse;
	}

	public Vaga convertToEntity(VagaRequest vagaRequest) {
		ModelMapper modelMapper = new ModelMapper();
		Vaga vaga = modelMapper.map(vagaRequest, Vaga.class);
		return vaga;
	}
}
