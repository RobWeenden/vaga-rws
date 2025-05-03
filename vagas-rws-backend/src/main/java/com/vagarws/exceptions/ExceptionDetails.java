package com.vagarws.exceptions;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * Class responsavel em padronizar o retorno das exception
 * 
 * @author robson.silva
 */
@Data
public class ExceptionDetails {

	private UUID errorId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timestamp;
	private Integer httpStatusCode;
	private String errorMessage;
	private String requestUri;
	private String errorDescription;

	public ExceptionDetails(LocalDateTime timestamp, Integer httpStatusCode, String errorMessage, String requestUri,
			String errorDescription) {
		super();
		this.errorId = UUID.randomUUID();
		this.timestamp = timestamp;
		this.httpStatusCode = httpStatusCode;
		this.errorMessage = errorMessage;
		this.requestUri = requestUri;
		this.errorDescription = errorDescription;
	}

}
