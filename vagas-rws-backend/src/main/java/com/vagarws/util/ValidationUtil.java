package com.vagarws.util;

/**
 * Classe responsavel em validar dados/objetos
 * 
 * @author robson.silva
 */
public class ValidationUtil {

	/**
	 * Método que valida se objeto String possui valor nulo/vazio e retorna true
	 * 
	 * @author robson.silva
	 * @param text
	 * @return
	 */
	public static boolean isNullOrEmpty(String text) {
		return (text == null || text.equals(ConstantsUtil.EMPTY) || text.isEmpty());
	}

}
