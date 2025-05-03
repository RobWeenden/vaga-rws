package com.vagarws.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vagarws.rest.entities.ResourceOwner;
import com.vagarws.rest.repository.UsuarioRepository;

/**
 * Class responsavel em implementar a interface UserDetailsService E verificar
 * se o login de acesso informado existe na base de dados
 * 
 * @author robson.silva
 */
@Service
public class UsuarioDetailService implements UserDetailsService {

	private UsuarioRepository userRepository;

	public UsuarioDetailService(UsuarioRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByCredenciais_Email(email).map(user -> new ResourceOwner(user)).orElseThrow(
				() -> new UsernameNotFoundException(String.format("O nome do usuário %s não foi encontrado.", email)));

	}

}
