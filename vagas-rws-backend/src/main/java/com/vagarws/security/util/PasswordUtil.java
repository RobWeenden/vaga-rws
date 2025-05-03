package com.vagarws.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Class responsavel em criptografar as senhas
 * 
 * @author robson.silva
 */
public class PasswordUtil {

	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	/**
	 * Metodo para criptografar senha
	 * 
	 * @author robson.silva
	 * @param senha
	 * @return
	 */
	public static String criptografarSenha(String senha) {
		return encoder.encode(senha);
	}

}
