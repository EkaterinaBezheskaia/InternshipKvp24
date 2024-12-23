package com.example.testtask.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

/**
 * DTO для представления показаний прибора учета.
 */
@Data
@Builder(builderMethodName = "fileDtoBuilder")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MeterReadingsDTO {

    /**
     * Уникальный идентификатор показаний.
     */
    Long id;

    /**
     * Дата показаний.
     */
    //@NotNull(message = "Значение не должно быть пустым")
    Month readingsDate;

    /**
     * Значение показаний прибора.
     * Должно быть в формате 0.001 с максимум 10 целыми и 3 дробными цифрами.
     */
    //@NotNull(message = "Значение не должно быть пустым")
    @Digits(integer = 10, fraction = 3, message = "Значение должно быть в формате 0.001")
    BigDecimal readings;

    /**
     * Дата и время создания показаний.
     */
    @NonNull
    @JsonProperty("creation_date")
    Instant createdAt = Instant.now();

    /**
     * Локальное время создания показаний.
     */
    @NonNull
    LocalDateTime createdAtLocal = LocalDateTime.now(ZoneId.systemDefault());

    /**
     * Дата и время последнего обновления показаний.
     */
    Instant updatedAt = Instant.now();
}

