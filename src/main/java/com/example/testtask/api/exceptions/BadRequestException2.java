package com.example.testtask.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException2 extends RuntimeException {
    public BadRequestException2(String message) {
        super(message);
    }
}
