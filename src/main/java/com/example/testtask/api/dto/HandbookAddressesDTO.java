package com.example.testtask.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder(builderMethodName = "fileDtoBuilder")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
//@Table(name = "HandbookAddresses")

public class HandbookAddressesDTO {

    @Column //(name = "column_0_id")
    @NonNull
    Long id;

    @Column //(name = "column_1_street")
    @NonNull
    String street;

    @Column //(name = "column_2_number")
    @NonNull
    Integer number;

    @Column //(name = "column_3_literal")
    String literal;

    @Column //(name = "column_4_flat")
    Integer flat;

    @Column //(name = "column_5_creation_date")
    @NonNull
    @JsonProperty("creation_date")
    Instant createdAt = Instant.now();

}
