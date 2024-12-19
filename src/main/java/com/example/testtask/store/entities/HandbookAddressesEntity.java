package com.example.testtask.store.entities;

import com.example.testtask.api.dto.HandbookAddressesDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HandbookAdresses")

public class HandbookAddressesEntity extends HandbookAddressesDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "column_0_id")
    private Long id;

    @Column (name = "column_1_street", nullable = false)
    private String street;

    @Column (name = "column_2_number", nullable = false)
    private Integer number;

    @Column (name = "column_3_literal")
    private String literal;

    @Column (name = "column_4_flat")
    private Integer flat;

    @Column (name = "column_5_creation_date", nullable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "address")
    private List<MetersEntity> meters;

}
