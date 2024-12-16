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
//@Table(name = "HandbookAdresses")

public class HandbookAddressesEntity extends HandbookAddressesDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column //(name = "column_0_id")
    private Long id;

    @Column (nullable = false)
    private String street;

    @Column (nullable = false)
    private Integer number;

    @Column //(name = "column_3_literal")
    private String literal;

    @Column //(name = "column_4_flat")
    private Integer flat;

    @Column (nullable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "address")
    private List<MetersEntity> meters;

}
