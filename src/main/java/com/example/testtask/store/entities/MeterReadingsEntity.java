package com.example.testtask.store.entities;

import com.example.testtask.api.dto.MeterReadingsDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Table(name = "files_base")

public class MeterReadingsEntity extends MeterReadingsDTO {

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
    @JoinColumn(name = "meter_id")
    private MetersEntity meter;

}