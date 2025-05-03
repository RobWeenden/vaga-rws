package com.vagarws.rest.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.vagarws.rest.controller.PerfilController;
import com.vagarws.rest.entities.Perfil;
import com.vagarws.rest.to.response.PerfilResponse;
import com.vagarws.rest.transfer.PerfilTransfer;

/**
 * Class responsavel em implementar a interface
 * {@link RepresentationModelAssembler} e gerar os links HATEOAS
 * 
 * @author robson.silva
 */
@Component
public class PerfilAssembler implements RepresentationModelAssembler<Perfil, EntityModel<PerfilResponse>> {

	@Override
	public EntityModel<PerfilResponse> toModel(Perfil perfil) {

		return EntityModel.of(PerfilTransfer.getPerfilResponse(perfil),
				linkTo(methodOn(PerfilController.class).findById(perfil.getId())).withSelfRel());
	}

}
