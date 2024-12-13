package com.example.testtask.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder(builderMethodName = "fileDtoBuilder")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class HandbookTypeMetersDTO {

    @NonNull
    Long id;

    @NonNull
    String titleTypeMeters;

    @NonNull
    @JsonProperty
    Instant createdAt;

}
