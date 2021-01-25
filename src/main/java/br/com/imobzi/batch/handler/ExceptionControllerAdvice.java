package br.com.imobzi.batch.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import br.com.imobzi.batch.domain.ResponseBodyDefaut;

@ControllerAdvice
public class ExceptionControllerAdvice {
	
	@MessageExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseBodyDefaut> handleException(BadRequestException badRequestException) {
		return new ResponseEntity<ResponseBodyDefaut>(
				ResponseBodyDefaut.builder()
				.status(false)
				.message(badRequestException.getMessage())
				.build(), HttpStatus.BAD_REQUEST);
    }
}
