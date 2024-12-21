package com.example.testtask.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

@Data
@Builder(builderMethodName = "fileDtoBuilder")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class MeterReadingsDTO {

    @NotNull(message = "ID не должен быть пустым")
    Long id;

    @NotNull(message = "Значение не должно быть пустым")
    Month readingsDate;

    @NotNull(message = "Значение не должно быть пустым")
    @Digits(integer = 10, fraction = 3, message = "Значение должно быть в формате 0.001")
    Double readings;

    @NonNull
    @JsonProperty("creation_date")
    Instant createdAt = Instant.now();

    @NonNull
    LocalDateTime createdAtLocal = LocalDateTime.now(ZoneId.systemDefault());

    Instant updatedAt = Instant.now();

}
