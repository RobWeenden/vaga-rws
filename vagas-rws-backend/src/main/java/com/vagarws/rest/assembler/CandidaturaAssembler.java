package com.vagarws.rest.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.vagarws.rest.controller.CandidaturaController;
import com.vagarws.rest.entities.Candidatura;
import com.vagarws.rest.entities.EtapaCandidatura;
import com.vagarws.rest.to.response.CandidaturaResponse;
import com.vagarws.rest.to.response.EtapaCandidaturaResponse;
import com.vagarws.rest.transfer.CandidaturaTransfer;
import com.vagarws.rest.transfer.EtapaCandidaturaTransfer;
import com.vagarws.rest.transfer.UsuarioTransfer;

@Component
public class CandidaturaAssembler
		implements RepresentationModelAssembler<Candidatura, EntityModel<CandidaturaResponse>> {

	@Override
	public EntityModel<CandidaturaResponse> toModel(Candidatura candidatura) {

		CandidaturaResponse candidaturaResponse = CandidaturaTransfer.getCandidaturaResponse(candidatura);
		return EntityModel.of(candidaturaResponse,
				linkTo(methodOn(CandidaturaController.class).findById(candidatura.getId())).withSelfRel(),
				linkTo(methodOn(CandidaturaController.class).getAll()).withSelfRel());
	}

	@Override
	public CollectionModel<EntityModel<CandidaturaResponse>> toCollectionModel(
			Iterable<? extends Candidatura> candidaturas) {
		var candidaturasCollections = StreamSupport.stream(candidaturas.spliterator(), false)
				.map(this::toModelWithSelfRel).collect(Collectors.toList());

		return CollectionModel.of(candidaturasCollections,
				linkTo(methodOn(CandidaturaController.class).getAll()).withSelfRel());
	}

	public EntityModel<CandidaturaResponse> toModelWithSelfRel(Candidatura candidatura) {

		CandidaturaResponse candidaturaResponse = CandidaturaTransfer.getCandidaturaResponse(candidatura);
		candidaturaResponse
				.setUsuarioCandidato(UsuarioTransfer.getUsuarioResponseMin(candidatura.getUsuarioCandidato()));
		candidaturaResponse
				.setUsuarioResponsavel(UsuarioTransfer.getUsuarioResponseMin(candidatura.getUsuarioResponsavel()));

		List<EtapaCandidaturaResponse> etapaCandidaturaResponses = new ArrayList<>();
		for (EtapaCandidatura etapaLoop : candidatura.getEtapas()) {
			EtapaCandidaturaResponse etapaCandidaturaResponse = EtapaCandidaturaTransfer
					.getCandidaturaResponse(etapaLoop);
			etapaCandidaturaResponses.add(etapaCandidaturaResponse);
		}
		candidaturaResponse.setEtapas(etapaCandidaturaResponses);

		return EntityModel.of(candidaturaResponse,
				linkTo(methodOn(CandidaturaController.class).findById(candidaturaResponse.getId())).withSelfRel());
	}

}