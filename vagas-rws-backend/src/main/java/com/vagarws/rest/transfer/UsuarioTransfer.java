package com.vagarws.rest.transfer;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;

import com.vagarws.rest.entities.Usuario;
import com.vagarws.rest.to.enums.PerfilUsuarioEnum;
import com.vagarws.rest.to.request.UsuarioRequest;
import com.vagarws.rest.to.response.UsuarioResponse;
import com.vagarws.util.ConstantsUtil;
import com.vagarws.util.ValidationUtil;

/**
 * Class responsavel em transferir os dados entre os objetos
 * 
 * @author robson.silva
 */
public class UsuarioTransfer {

	public static UsuarioResponse getUsuarioResponse(Usuario usuario) {
		UsuarioResponse usuarioResponse = new UsuarioResponse();
		usuarioResponse.setId(usuario.getId());
		usuarioResponse.setAtivo(usuario.getAtivo());
		usuarioResponse.setCpf(usuario.getCpf());
		usuarioResponse.setNome(usuario.getNome());
		usuarioResponse.setDataNascimento(usuario.getDataNascimento());
		usuarioResponse.setCargo(usuario.getCargo());
		usuarioResponse.setDepartamento(usuario.getDepartamento());
		usuarioResponse.setEmail(usuario.getCredenciais().getEmail());
		usuarioResponse.setTelefone(usuario.getTelefone());
		usuarioResponse.setDataCadastro(usuario.getDataCadastro());

		String roles = usuario.getListaPerfil().stream()
				.filter(perfil -> !ValidationUtil.isNullOrEmpty(perfil.getAuthority()))
				.map(GrantedAuthority::getAuthority).collect(Collectors.joining(ConstantsUtil.JOIN_VIRGULA));

		usuarioResponse.setPerfil(PerfilUsuarioEnum.fromValor(roles));

		return usuarioResponse;
	}

	public Usuario convertToEntity(UsuarioRequest usuarioRequest) {
		ModelMapper modelMapper = new ModelMapper();
		Usuario usuario = modelMapper.map(usuarioRequest, Usuario.class);
		return usuario;
	}

	public static UsuarioResponse getUsuarioResponseMin(Usuario usuario) {
		UsuarioResponse usuarioResponse = new UsuarioResponse();
		usuarioResponse.setId(usuario.getId());
		usuarioResponse.setAtivo(usuario.getAtivo());
		usuarioResponse.setNome(usuario.getNome());

		return usuarioResponse;
	}

}
