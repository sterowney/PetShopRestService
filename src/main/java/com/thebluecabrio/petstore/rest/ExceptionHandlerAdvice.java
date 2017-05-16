package com.thebluecabrio.petstore.rest;

import com.thebluecabrio.petstore.exceptions.CategoryNotFoundException;
import com.thebluecabrio.petstore.exceptions.PetNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by stevenrowney on 16/05/2017.
 */
@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);
    private static final String INTERNAL_SERVER_ERROR = "Something went wrong on our side";

    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<Object> handleAccessDenied(AccessDeniedException e, WebRequest request) {

        return handleExceptionInternal(e, new Error(HttpStatus.FORBIDDEN.value(), e.getMessage()), getHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({CategoryNotFoundException.class, PetNotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundRequest(RuntimeException e, WebRequest request) {

        return handleExceptionInternal(e, new Error(HttpStatus.NOT_FOUND.value(), e.getMessage()), getHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleInternalServerException(Exception e, WebRequest request) {

        LOG.error(INTERNAL_SERVER_ERROR, e);

        return handleExceptionInternal(e, new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR), getHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private HttpHeaders getHeaders() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }
}

@Getter
@AllArgsConstructor
class Error {

    private int code;
    private String message;
}
