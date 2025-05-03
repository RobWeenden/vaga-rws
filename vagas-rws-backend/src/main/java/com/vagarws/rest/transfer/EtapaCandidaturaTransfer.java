package com.vagarws.rest.transfer;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;

import com.vagarws.rest.entities.EtapaCandidatura;
import com.vagarws.rest.to.request.EtapaCandidaturaRequest;
import com.vagarws.rest.to.response.EtapaCandidaturaResponse;

/**
 * Class responsavel em transferir os dados entre os objetos
 * 
 * @author robson.silva
 */
public class EtapaCandidaturaTransfer {

	public static EtapaCandidaturaResponse getCandidaturaResponse(EtapaCandidatura etapaCandidatura) {
		EtapaCandidaturaResponse etapaCandidaturaResponse = new EtapaCandidaturaResponse();
		BeanUtils.copyProperties(etapaCandidatura, etapaCandidaturaResponse);
		return etapaCandidaturaResponse;
	}

	public EtapaCandidatura convertToEntity(EtapaCandidaturaRequest etapaCandidaturaRequest) {
		ModelMapper modelMapper = new ModelMapper();
		EtapaCandidatura etapaCandidatura = modelMapper.map(etapaCandidaturaRequest, EtapaCandidatura.class);
		return etapaCandidatura;
	}
}
