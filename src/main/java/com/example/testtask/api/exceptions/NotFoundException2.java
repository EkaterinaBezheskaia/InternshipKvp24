package com.example.testtask.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое выбрасывается, когда запрашиваемый ресурс не найден.
 * Устанавливает статус ответа HTTP на 404 (NOT FOUND).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException2 extends RuntimeException {

    /**
     * Конструктор для создания исключения с заданным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public NotFoundException2(String message) {
        super(message);
    }
}
