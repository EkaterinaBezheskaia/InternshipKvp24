package com.example.testtask.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * DTO для представления прибора учета.
 */
@Data
@Builder(builderMethodName = "fileDtoBuilder")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MetersDTO {

    /**
     * Уникальный идентификатор прибора учета.
     */
    @NotNull(message = "ID не должен быть пустым")
    Long id;

    /**
     * Серийный номер прибора учета.
     */
    @NotNull(message = "Серийный номер прибора не должен быть пустым")
    @Size(max = 100, message = "Серийный номер прибора не должен превышать 100 символов")
    String metersSerialNumber;

    /**
     * Дата установки прибора учета.
     */
    @NotNull(message = "Дата установки прибора не должна быть пустой")
    LocalDate installationDate;

    /**
     * Дата и время создания записи о приборе учета.
     */
    @NonNull
    @JsonProperty("creation_date")
    Instant createdAt = Instant.now();

    /**
     * Локальное время создания записи о приборе учета.
     */
    @NonNull
    LocalDateTime createdAtLocal = LocalDateTime.now(ZoneId.systemDefault());

    /**
     * Дата и время последнего обновления записи о приборе учета.
     */
    @NonNull
    Instant updatedAt = Instant.now();
}
