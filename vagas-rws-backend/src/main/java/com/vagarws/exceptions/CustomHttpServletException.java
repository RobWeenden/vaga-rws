package com.vagarws.exceptions;

import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Class responsavel gerenciar as exceptions especificas do filter de
 * autenticação
 * 
 * @author robson.silva
 */
public class CustomHttpServletException {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	public CustomHttpServletException(HttpServletResponse response, HttpServletRequest request, String message,
			HttpStatus status) {
		super();
		this.httpServletResponseException(response, request, message, status);
	}

	/**
	 * Método para tratar a exceptions de token
	 * 
	 * @author robson.silva
	 * @param response
	 * @param request
	 * @param ex
	 */
	private void httpServletResponseException(HttpServletResponse response, HttpServletRequest request, String message,
			HttpStatus status) {
		try {
			response.setStatus(status.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);

			ExceptionDetails details = new ExceptionDetails(LocalDateTime.now(), status.value(), message,
					"uri=" + request.getRequestURI(), status.getReasonPhrase());

			ObjectMapper mapper = new ObjectMapper();
			mapper.findAndRegisterModules();
			String jsonResponse;
			jsonResponse = mapper.writeValueAsString(details);
			response.getWriter().write(jsonResponse);
		} catch (IOException e) {
			logger.error("ERROR: error ao tratar exception do JwtAuthFilter.java verificar a exception:", e);
		}
	}

}
