package com.vagarws.exceptions;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Class responsavel em gerenciar as exceptions
 * 
 * @author robson.silva
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * Método para validar as exceptions de validação ao realizar as requisições
	 * 
	 * @author robson.silva
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<String> errorMessages = ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage)
				.toList();
		String msg = String.join("; ", errorMessages);
		ExceptionDetails details = new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), msg,
				request.getDescription(false), HttpStatus.BAD_REQUEST.getReasonPhrase());

		logger.warn("Validation error: {}", details);
		return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Método para validar exceptions genericas
	 * 
	 * @author robson.silva
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request) {
		String msg;
		if (ex instanceof MethodArgumentNotValidException) {
			List<ObjectError> errors = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
			msg = errors.stream().map(ObjectError::getDefaultMessage).reduce((s1, s2) -> s1 + "; " + s2)
					.orElse("Erro de validação desconhecido.");
		} else if (ex instanceof HttpMessageNotReadableException) {
			msg = "Não há dados sendo enviados no corpo da requisição.";
		} else {
			msg = ex.getMessage();
		}

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		if (ex instanceof BadCredentialsException || ex instanceof HttpMessageNotReadableException) {
			status = HttpStatus.BAD_REQUEST;
		}

		ExceptionDetails details = new ExceptionDetails(LocalDateTime.now(), status.value(), msg,
				request.getDescription(false), status.getReasonPhrase());

		if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
			logger.error("Exception handled: {}", details, ex);
		} else {
			logger.warn("Exception handled: {}", details);
		}

		return new ResponseEntity<>(details, status);

	}

	/**
	 * Método para exceptions customizadas nas requisições
	 * 
	 * @author robson.silva
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?> handleCustomException(CustomException ex, WebRequest request) {
		ExceptionDetails details = new ExceptionDetails(LocalDateTime.now(), ex.getStatus().value(), ex.getMessage(),
				request.getDescription(false), ex.getStatus().getReasonPhrase());
		return new ResponseEntity<>(details, ex.getStatus());
	}

}
