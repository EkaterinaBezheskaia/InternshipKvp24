package com.example.testtask.store.entities;

import com.example.testtask.api.dto.MetersDTO;
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

public class MetersEntity extends MetersDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titleMetersNumber;

//        @Builder.Default
//        String base64File = "";

    @Builder.Default
    Instant creationDate = Instant.now();

    @Builder.Default
    private String description = "";

    @ManyToOne
    @JoinColumn(name = "address_id")
    private HandbookAddressesEntity address;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private HandbookTypeMetersEntity type;

    @OneToMany(mappedBy = "meter")
    private List<MeterReadingsEntity> readings;

}