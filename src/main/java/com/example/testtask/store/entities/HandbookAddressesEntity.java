package com.example.testtask.store.entities;

import com.example.testtask.api.dto.HandbookAddressesDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "handbook_adresses")

public class HandbookAddressesEntity extends HandbookAddressesDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column (name = "column_0_id")
    private long id;

    @Column (name = "column_1_street", nullable = false)
    private String street;

    @Column (name = "column_2_number", nullable = false)
    private int number;

    @Column (name = "column_3_literal")
    private String literal;

    @Column (name = "column_4_flat")
    private int flat;

    @Column (name = "column_5_utc_date", nullable = false)
    private Instant createdAt = Instant.now();

    @Column (name = "column_6_located_date", nullable = false)
    private LocalDateTime createdAtLocal = LocalDateTime.now(ZoneId.systemDefault());

    @Column (name = "column_7_updated_date")
    Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "address")
    private List<MetersEntity> meters;

}
