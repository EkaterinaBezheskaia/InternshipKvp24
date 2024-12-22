package com.example.testtask.api.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Обработчик исключений для всего приложения.
 * Перехватывает исключения и возвращает соответствующие ответы.
 */
@Log4j2
@ControllerAdvice
public class FileExceptionHandler extends ResponseStatusExceptionHandler {

    /**
     * Обрабатывает все исключения, не попадающие под другие обработчики.
     *
     * @param ex     исключение, которое произошло
     * @param request объект запроса, содержащий информацию о запросе
     * @return ResponseEntity с сообщением об ошибке и статусом 403 (FORBIDDEN)
     */
    @ExceptionHandler(Exception.class)
    @Nullable
    public ResponseEntity<Object> exception(Exception ex, WebRequest request) {
        log.error("Произошло исключение: ", ex);

        String errorMessage = "Доступ отклонен: " + ex.getMessage();
        return new ResponseEntity<>(
                errorMessage,
                new HttpHeaders(),
                HttpStatus.FORBIDDEN
        );
    }

    /**
     * Обрабатывает исключения валидации.
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
}