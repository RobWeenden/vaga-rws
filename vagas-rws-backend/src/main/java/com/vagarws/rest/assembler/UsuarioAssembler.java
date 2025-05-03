package com.vagarws.rest.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.vagarws.rest.controller.UsuarioController;
import com.vagarws.rest.entities.Usuario;
import com.vagarws.rest.to.response.UsuarioResponse;
import com.vagarws.rest.transfer.UsuarioTransfer;

@Component
public class UsuarioAssembler implements RepresentationModelAssembler<Usuario, EntityModel<UsuarioResponse>> {

	@Override
	public EntityModel<UsuarioResponse> toModel(Usuario usuario) {

		UsuarioResponse usuarioResponse = UsuarioTransfer.getUsuarioResponse(usuario);
		return EntityModel.of(usuarioResponse,
				linkTo(methodOn(UsuarioController.class).findById(usuarioResponse.getId())).withSelfRel(),
				linkTo(methodOn(UsuarioController.class).getAll()).withSelfRel());
	}

	@Override
	public CollectionModel<EntityModel<UsuarioResponse>> toCollectionModel(Iterable<? extends Usuario> usuarios) {
		var usuariosCollections = StreamSupport.stream(usuarios.spliterator(), false).map(this::toModelWithSelfRel)
				.collect(Collectors.toList());

		return CollectionModel.of(usuariosCollections,
				linkTo(methodOn(UsuarioController.class).getAll()).withSelfRel());
	}

	public EntityModel<UsuarioResponse> toModelWithSelfRel(Usuario usuario) {

		UsuarioResponse usuarioResponse = UsuarioTransfer.getUsuarioResponse(usuario);
		return EntityModel.of(usuarioResponse,
				linkTo(methodOn(UsuarioController.class).findById(usuarioResponse.getId())).withSelfRel());
	}

}
