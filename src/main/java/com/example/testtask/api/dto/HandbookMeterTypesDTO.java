package com.example.testtask.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * DTO для представления типа прибора учета.
 */
@Data
@Builder(builderMethodName = "fileDtoBuilder")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HandbookMeterTypesDTO {

    /**
     * Уникальный идентификатор типа прибора.
     */
    Long id;

    /**
     * Название типа прибора учета.
     */
    //@NotNull(message = "Название типа прибора не должно быть пустым")
    @Size(max = 100, message = "Название типа прибора не должно превышать 100 символов")
    @Pattern(regexp ="^[А-Я].+", message = "Должно начинаться с заглавной буквы.")
    String meterTypeTitle;

    /**
     * Дата и время создания типа прибора.
     */
    @NonNull
    @JsonProperty("creation_date")
    Instant createdAt = Instant.now();

    /**
     * Локальное время создания типа прибора.
     */
    @NonNull
    LocalDateTime createdAtLocal = LocalDateTime.now(ZoneId.systemDefault());

    /**
     * Дата и время последнего обновления типа прибора.
     */
    Instant updatedAt = Instant.now();
}
