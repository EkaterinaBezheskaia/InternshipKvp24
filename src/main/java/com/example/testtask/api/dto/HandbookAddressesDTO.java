package com.example.testtask.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Builder(builderMethodName = "fileDtoBuilder")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class HandbookAddressesDTO {

    @NotNull(message = "ID не должен быть пустым")
    long id;

    @NotNull(message = "Улица не должна быть пустой")
    @Size(max = 100, message = "Название улицы не должно превышать 100 символов")
    String street;

    @NotNull(message = "Номер не должен быть пустым")
    @Min(value = 0, message = "Номер дома должен быть положительным")
    int number;

    @Size(max = 1, message = "Литерал не должен превышать 1 символа")
    String literal;

    @Min(value = 0, message = "Номер квартиры должен быть положительным")
    int flat;

    @NonNull
    @JsonProperty("creation_date")
    Instant createdAt = Instant.now();

    Instant updatedAt = Instant.now();

    @NonNull
    LocalDateTime createdAtLocal = LocalDateTime.now(ZoneId.systemDefault());

}
