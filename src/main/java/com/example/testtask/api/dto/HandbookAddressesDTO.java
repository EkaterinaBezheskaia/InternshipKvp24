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

public class HandbookAddressesDTO {

    @NonNull
    Long id;

    @NonNull
    String titleAddress;

    @NonNull
    String literal;

    @NonNull
    @JsonProperty("creation_date")
    Instant creationDate = Instant.now();

    @NonNull
    String description;

}
