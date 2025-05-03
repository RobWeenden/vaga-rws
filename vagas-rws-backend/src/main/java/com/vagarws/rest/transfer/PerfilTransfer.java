package com.vagarws.rest.transfer;

import com.vagarws.rest.entities.Perfil;
import com.vagarws.rest.to.response.PerfilResponse;

/**
 * Class responsavel em transferir os dados entre os objetos
 * 
 * @author robson.silva
 */
public class PerfilTransfer {

	public static PerfilResponse getPerfilResponse(Perfil perfil) {
		PerfilResponse perfilResponse = new PerfilResponse();
		perfilResponse.setId(perfil.getId());
		perfilResponse.setPerfil(perfil.getPerfil());
		return perfilResponse;
	}

}
