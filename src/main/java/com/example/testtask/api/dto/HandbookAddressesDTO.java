package com.example.testtask.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * DTO для представления адреса в справочнике.
 */
@Data
@Builder(builderMethodName = "fileDtoBuilder")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HandbookAddressesDTO {

    /**
     * Уникальный идентификатор адреса.
     */
    Long id;

    /**
     * Название улицы.
     */
    @Size(max = 100, message = "Название улицы не должно превышать 100 символов")
    @Pattern(regexp ="^[А-Я].+", message = "Должно начинаться с заглавной буквы.")
    String street;

    /**
     * Номер дома.
     */
    @Min(value = 0, message = "Номер дома должен быть положительным")
    Integer number;

    /**
     * Литерал адреса (необязательный).
     */
    @Size(max = 1, message = "Литерал не должен превышать 1 символа")
    @Pattern(regexp = "^[А-ЯЁ]+$", message = "Должно состоять только из заглавных букв.")
    String literal;

    /**
     * Номер квартиры (необязательный).
     */
    @Min(value = 0, message = "Номер квартиры должен быть положительным")
    Integer flat;

    /**
     * Дата и время создания адреса.
     */
    @NonNull
    @JsonProperty("creation_date")
    Instant createdAt = Instant.now();

    /**
     * Дата и время последнего обновления адреса.
     */
    Instant updatedAt = Instant.now();

    /**
     * Локальное время создания адреса.
     */
    @NonNull
    LocalDateTime createdAtLocal = LocalDateTime.now(ZoneId.systemDefault());
}
