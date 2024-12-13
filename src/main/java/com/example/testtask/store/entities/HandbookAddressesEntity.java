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

public class HandbookAddressesEntity extends HandbookAddressesDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String street;

    @Column (nullable = false)
    private Integer number;

    private String literal = "";

    private Integer flat;

    @Column (nullable = false)
    Instant creationDate = Instant.now();

    @OneToMany(mappedBy = "address")
    private List<MetersEntity> meters;

}
