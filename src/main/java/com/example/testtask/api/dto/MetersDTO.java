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

@Data
@Builder(builderMethodName = "fileDtoBuilder")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class MetersDTO {

    @NotNull(message = "ID не должен быть пустым")
    Long id;

    @NotNull(message = "Серийный номер прибора не должен быть пустым")
    @Size(max = 100, message = "Серийный номер прибора не должен превышать 100 символов")
    String metersSerialNumber;

    @NotNull(message = "Дата установки прибора не должна быть пустой")
    LocalDate installationDate;

    @NonNull
    @JsonProperty("creation_date")
    Instant createdAt = Instant.now();

    @NonNull
    LocalDateTime createdAtLocal = LocalDateTime.now(ZoneId.systemDefault());

    @NonNull
    Instant updatedAt = Instant.now();

}
