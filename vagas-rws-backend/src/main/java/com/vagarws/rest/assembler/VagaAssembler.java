package com.vagarws.rest.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.vagarws.rest.controller.VagaController;
import com.vagarws.rest.entities.Vaga;
import com.vagarws.rest.to.response.VagaResponse;
import com.vagarws.rest.transfer.VagaTransfer;

@Component
public class VagaAssembler implements RepresentationModelAssembler<Vaga, EntityModel<VagaResponse>> {

	@Override
	public EntityModel<VagaResponse> toModel(Vaga vaga) {

		VagaResponse vagaResponse = VagaTransfer.getVagaResponse(vaga);
		return EntityModel.of(vagaResponse, linkTo(methodOn(VagaController.class).findById(vaga.getId())).withSelfRel(),
				linkTo(methodOn(VagaController.class).getAll()).withSelfRel());
	}

	@Override
	public CollectionModel<EntityModel<VagaResponse>> toCollectionModel(Iterable<? extends Vaga> vagas) {
		var vagasCollections = StreamSupport.stream(vagas.spliterator(), false).map(this::toModelWithSelfRel)
				.collect(Collectors.toList());

		return CollectionModel.of(vagasCollections, linkTo(methodOn(VagaController.class).getAll()).withSelfRel());
	}

	public EntityModel<VagaResponse> toModelWithSelfRel(Vaga vaga) {

		VagaResponse vagaResponse = VagaTransfer.getVagaResponse(vaga);
		return EntityModel.of(vagaResponse,
				linkTo(methodOn(VagaController.class).findById(vagaResponse.getId())).withSelfRel());
	}

}