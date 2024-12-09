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
//@Table(name = "files_base")

public class HandbookAddressesEntity extends HandbookAddressesDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column //(unique = true)
    private String titleAdress;

    @Builder.Default
    private String literal = "";

//        @Builder.Default
//        String base64File = "";

    @Builder.Default
    Instant creationDate = Instant.now();

    @Builder.Default
    private String description = "";

    @OneToMany(mappedBy = "address")
    private List<MetersEntity> meters;

}
