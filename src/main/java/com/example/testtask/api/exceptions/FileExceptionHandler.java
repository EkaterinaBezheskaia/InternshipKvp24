package com.example.testtask.api.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Обработчик исключений для всего приложения.
 * Перехватывает исключения и возвращает соответствующие ответы.
 */
@Log4j2
@ControllerAdvice
public class FileExceptionHandler {

    /**
     * Обработчик исключений для NotFoundException2.
     *
     * @param ex      исключение, которое было выброшено
     * @param request объект запроса, содержащий информацию о текущем запросе
     * @return ResponseEntity с сообщением об ошибке и статусом 404 (Not Found)
     */
    @ExceptionHandler(NotFoundException2.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException2 ex, WebRequest request) {
        log.error("Не найдено: ", ex);
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    /**
     * Обработчик исключений для BadRequestException2.
     *
     * @param ex      исключение, которое было выброшено
     * @param request объект запроса, содержащий информацию о текущем запросе
     * @return ResponseEntity с сообщением об ошибке и статусом 400 (Bad Request)
     */
    @ExceptionHandler(BadRequestException2.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException2 ex, WebRequest request) {
        log.error("Ошибка запроса: ", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    /**
     * Обрабатывает исключения валидации, возникающие при неверных данных в запросе.
     *
     * @param ex исключение, связанное с ошибками валидации
     * @return ResponseEntity с информацией об ошибках валидации и статусом 400 (BAD REQUEST)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Ошибка валидации: ", ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        StringBuilder errorMessage = new StringBuilder("Не удалось выполнить проверку: ");
        errors.forEach((field, message) ->
                errorMessage.append(field).append(": ").append(message).append("; ")
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorDTO.builder()
                        .error("Ошибка валидации")
                        .errorDescription(errorMessage.toString())
                        .build());
    }

    /**
     * Обрабатывает все остальные исключения, не попадающие под другие обработчики.
     *
     * @param ex      исключение, которое произошло
     * @param request объект запроса, содержащий информацию о текущем запросе
     * @return ResponseEntity с сообщением об ошибке и статусом 500 (INTERNAL SERVER ERROR)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        log.error("Произошло исключение: ", ex);
        return buildResponseEntity("Произошла ошибка: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Утилитарный метод для создания ResponseEntity.
     */
    private ResponseEntity<Object> buildResponseEntity(String message, HttpStatus status) {
        return new ResponseEntity<>(message, new HttpHeaders(), status);
    }
}