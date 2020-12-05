package br.com.ilia.digital.folhadeponto.service.exception;

import org.springframework.http.HttpStatus;

public class FolhaDePontoException extends RuntimeException {

	private HttpStatus httpStatus;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8947843560548062711L;

	public FolhaDePontoException(String message, HttpStatus httpStatus) {
		super(message);
		this.setHttpStatus(httpStatus);
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

}
