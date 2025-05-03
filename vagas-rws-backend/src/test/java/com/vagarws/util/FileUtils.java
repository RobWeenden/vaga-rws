package com.vagarws.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Classe util para manipular arquivos e transformar os dados
 * 
 * @author robson.silva
 */
public class FileUtils {

	private static final ObjectMapper objectMapper;

	/**
	 * Bloco de inicalização estatico será executado criar o ObjectMapper e
	 * registraro modulo do JavaTimeModule para lidar com campos que possui
	 * datas/horas e minutos como classes {@link LocalDate} {@link LocalDateTime}
	 * Assim evita o ERROR: "Java 8 date/time type java.time.LocalDate not supported
	 * by default: add Module
	 * com.fasterxml.jackson.datatype:jackson-datatype-jsr310"
	 * 
	 * @author robson.silva
	 */
	static {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	/**
	 * Método transforma os dados do arquivo conforme é informado pela path do
	 * arquivo e nome
	 * 
	 * @author robson.silva
	 * @param file
	 * @return
	 */
	public static String fetchScript(String path) {
		InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(path);
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();

	}

	/**
	 * Método generico para transformar os dados de arquivo em um objeto conforme a
	 * path do arquivo e nome
	 * 
	 * @author robson.silva
	 * @param <T>
	 * @param pathFile
	 * @param valueType
	 * @return
	 */
	public static <T> T fetchScript(String pathFile, Class<T> valueType) {
		try {

			InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(pathFile);
			if (inputStream == null) {
				throw new IllegalArgumentException("Arquivo não encontrado: " + pathFile);
			}

			return objectMapper.readValue(inputStream, valueType);
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;

	}

	public static <T> T fetchScript(String pathFile, TypeReference<T> typeReference) {
		try {
			InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(pathFile);
			if (inputStream == null) {
				throw new IllegalArgumentException("Arquivo não encontrado: " + pathFile);
			}

			// Usa o TypeReference para desserialização
			return objectMapper.readValue(inputStream, typeReference);
		} catch (Exception e) {
			e.printStackTrace(); // Corrige para printar o stack trace
		}
		return null;
	}
}
