package com.example.testtask.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder(builderMethodName = "fileDtoBuilder")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class MetersDTO {

    @NonNull
    Long id;

    @NonNull
    String metersSerialNumber;

    @NonNull
    LocalDate installationDate;

    @NonNull
    @JsonProperty
    Instant creationDate;

}
