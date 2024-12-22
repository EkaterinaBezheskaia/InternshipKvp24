package com.example.testtask.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое выбрасывается при возникновении ошибки запроса.
 * Устанавливает статус ответа HTTP на 400 (BAD REQUEST).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException2 extends RuntimeException {

    /**
     * Конструктор для создания исключения с заданным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public BadRequestException2(String message) {
        super(message);
    }
}