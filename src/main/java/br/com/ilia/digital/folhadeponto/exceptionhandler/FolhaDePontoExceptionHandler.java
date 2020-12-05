package br.com.ilia.digital.folhadeponto.exceptionhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.ilia.digital.folhadeponto.service.exception.FolhaDePontoException;
import br.com.ilia.digital.folhadeponto.service.util.MensagensUtil;

import java.time.DateTimeException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class FolhaDePontoExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MensagensUtil mensagensUtil;

	@ExceptionHandler(FolhaDePontoException.class)
	public ResponseEntity<Object> handleFolhaDePontoException(FolhaDePontoException ex, WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("mensagem", ex.getMessage());

		return new ResponseEntity<>(body, ex.getHttpStatus());
	}
	
	@ExceptionHandler(DateTimeException.class)
	public ResponseEntity<Object> handleDateTimeException(DateTimeException ex, WebRequest request) {
		
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("mensagem", mensagensUtil.getProperty("data.hora.invalida"));
		
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, 
			HttpStatus status, WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("mensagem", ex.getMessage());

		List<String> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		body.put("erros", errors);

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
}
