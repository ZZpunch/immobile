package br.com.imobzi.batch.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import br.com.imobzi.batch.domain.ExceptionResponseBody;

@ControllerAdvice
public class ExceptionControllerAdvice {
	
	@MessageExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponseBody> handleException(BadRequestException badRequestException) {
		return new ResponseEntity<ExceptionResponseBody>(
				ExceptionResponseBody.builder()
				.status(false)
				.message(badRequestException.getMessage())
				.build(), HttpStatus.BAD_REQUEST);
    }
}
