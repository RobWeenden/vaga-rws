package com.vagarws.rest.transfer;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;

import com.vagarws.rest.entities.Candidatura;
import com.vagarws.rest.to.request.CandidaturaRequest;
import com.vagarws.rest.to.response.CandidaturaResponse;

/**
 * Class responsavel em transferir os dados entre os objetos
 * 
 * @author robson.silva
 */
public class CandidaturaTransfer {

	public static CandidaturaResponse getCandidaturaResponse(Candidatura candidatura) {
		CandidaturaResponse candidaturaResponse = new CandidaturaResponse();
		BeanUtils.copyProperties(candidatura, candidaturaResponse);
		return candidaturaResponse;
	}

	public Candidatura convertToEntity(CandidaturaRequest candidaturaRequest) {
		ModelMapper modelMapper = new ModelMapper();
		Candidatura candidatura = modelMapper.map(candidaturaRequest, Candidatura.class);
		return candidatura;
	}
}
