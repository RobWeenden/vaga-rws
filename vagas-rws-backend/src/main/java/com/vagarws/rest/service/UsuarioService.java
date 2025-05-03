package com.vagarws.rest.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vagarws.exceptions.CustomException;
import com.vagarws.rest.entities.Credenciais;
import com.vagarws.rest.entities.Perfil;
import com.vagarws.rest.entities.Usuario;
import com.vagarws.rest.repository.UsuarioRepository;
import com.vagarws.rest.to.enums.PerfilUsuarioEnum;
import com.vagarws.rest.to.request.UsuarioRequest;
import com.vagarws.rest.util.CustomMapperUtil;
import com.vagarws.security.repository.UsuarioTokenRepository;
import com.vagarws.security.util.PasswordUtil;
import com.vagarws.util.ConstantsUtil;
import com.vagarws.util.ValidationUtil;

/**
 * Class responsavel em realizar os servicos como beans do Spring
 * 
 * @author robson.silva
 */
@Service
public class UsuarioService {

	private UsuarioRepository repository;
	private PerfilService perfilService;
	private CustomMapperUtil mapperUtil;
	private UsuarioTokenRepository usuarioTokenRepository;

	public UsuarioService(UsuarioRepository repository, PerfilService perfilService, CustomMapperUtil mapperUtil,
			UsuarioTokenRepository usuarioTokenRepository) {
		super();
		this.repository = repository;
		this.perfilService = perfilService;
		this.mapperUtil = mapperUtil;
		this.usuarioTokenRepository = usuarioTokenRepository;
	}

	public Usuario create(UsuarioRequest usuarioRequest) {

		try {
			Perfil perfil = this.perfilService.findByPerfil(PerfilUsuarioEnum.fromRole(usuarioRequest.getPerfil()),
					ConstantsUtil.PERFIL_NOT_FOUND, HttpStatus.NOT_FOUND);

			if (!ValidationUtil.isNullOrEmpty(usuarioRequest.getSenha())) {
				usuarioRequest.setSenha(PasswordUtil.criptografarSenha(usuarioRequest.getSenha()));
			}
			Usuario usuario = new Usuario(usuarioRequest, perfil);
			usuario.setDataCadastro(LocalDate.now());
			return this.repository.save(usuario);

		} catch (DataIntegrityViolationException ex) {
			throw new CustomException(ConstantsUtil.EMAIL_USUARIO_EXISTENTE, HttpStatus.CONFLICT);
		}
	}

	public Usuario findById(String id) {
		return this.repository.findById(id)
				.orElseThrow(() -> new CustomException(ConstantsUtil.NOT_FOUND_MSG, HttpStatus.NOT_FOUND));
	}

	public List<Usuario> getAll() {
		return this.repository.findAll();
	}

	public void delete(String id) {
		Usuario usuario = this.repository.findById(id)
				.orElseThrow(() -> new CustomException(ConstantsUtil.NOT_FOUND_MSG, HttpStatus.NOT_FOUND));
		usuario.getListaPerfil().clear();
		this.repository.save(usuario);
		this.usuarioTokenRepository.deleteTokenByIdUsuario(id);
		this.repository.deleteById(id);
	}

	public Usuario update(UsuarioRequest usuarioRequest) {
		Usuario usuario = this.repository.findById(usuarioRequest.getId())
				.orElseThrow(() -> new CustomException(ConstantsUtil.NOT_FOUND_MSG, HttpStatus.NOT_FOUND));

		if (!ValidationUtil.isNullOrEmpty(usuarioRequest.getSenha())) {
			usuarioRequest.setSenha(PasswordUtil.criptografarSenha(usuarioRequest.getSenha()));
			usuario.setCredenciais(new Credenciais(usuarioRequest.getEmail(), usuarioRequest.getSenha()));
		}

		if (!ValidationUtil.isNullOrEmpty(usuarioRequest.getPerfil())) {
			Perfil perfil = this.perfilService.findByPerfil(PerfilUsuarioEnum.fromRole(usuarioRequest.getPerfil()),
					ConstantsUtil.PERFIL_NOT_FOUND, HttpStatus.NOT_FOUND);
			usuario.getListaPerfil().clear();
			usuario.getListaPerfil().add(perfil);
		}
		this.mapperUtil.updateTOEntity(usuarioRequest, usuario);
		return this.repository.save(usuario);

	}

	public boolean existsEmail(String email, String msgException, HttpStatus httpStatus) {
		if (this.repository.existsByCredenciais_Email(email)) {
			throw new CustomException(msgException, httpStatus);
		}
		return false;
	}

	public Usuario save(Usuario usuario) {
		return this.repository.save(usuario);
	}

	public List<Usuario> getAllUsuariosWithOutPerfilColaborador() {
		return this.repository.getAllUsuariosWithOutPerfilColaborador();
	}

}
