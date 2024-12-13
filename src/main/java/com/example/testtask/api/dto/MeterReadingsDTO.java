package com.example.testtask.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;

@Data
@Builder(builderMethodName = "fileDtoBuilder")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class MeterReadingsDTO {

    @NonNull
    Long id;

    @NonNull
    private Month readingsDate;

    @NonNull
    @JsonProperty
    Instant createdAt;

    @NonNull
    Long readings;

}
