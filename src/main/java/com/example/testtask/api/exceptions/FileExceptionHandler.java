package com.example.testtask.api.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;


@Log4j2
@ControllerAdvice

public class FileExceptionHandler extends ResponseStatusExceptionHandler {

    @ExceptionHandler(Exception.class)
    @Nullable
    public ResponseEntity<Object> exception(Exception ex, WebRequest request) {

        log.error("Exception occurred: ", ex);

        String errorMessage = "Access denied: " + ex.getMessage();
        return new ResponseEntity<>(
                errorMessage,
                new HttpHeaders(),
                HttpStatus.FORBIDDEN
        );

    }
}
