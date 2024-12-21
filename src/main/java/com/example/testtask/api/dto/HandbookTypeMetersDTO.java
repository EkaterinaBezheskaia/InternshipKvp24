package com.example.testtask.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

public class HandbookTypeMetersDTO {

    @NotNull(message = "ID не должен быть пустым")
    Long id;

    @NotNull(message = "Название типа прибора не должно быть пустым")
    @Size(max = 100, message = "Название типа прибора не должно превышать 100 символов")
    String titleTypeMeters;

    @NonNull
    @JsonProperty("creation_date")
    Instant creationDate = Instant.now();

    @NonNull
    LocalDateTime createdAtLocal = LocalDateTime.now(ZoneId.systemDefault());

}
