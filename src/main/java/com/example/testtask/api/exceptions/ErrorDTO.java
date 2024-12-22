package com.example.testtask.api.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO для представления информации об ошибке.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorDTO {

    /**
     * Краткое описание ошибки.
     */
    String error;

    /**
     * Подробное описание ошибки.
     */
    @JsonProperty("error_description")
    String errorDescription;
}
