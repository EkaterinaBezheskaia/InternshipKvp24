package com.example.testtask.store.entities;

import com.example.testtask.api.dto.MeterReadingsDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.Month;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Month readingsDate;

    @Builder.Default
    Instant creationDate = Instant.now();

    @Column (nullable = false)
    private Long readings;

    @ManyToOne
    @JoinColumn //(name = "meter_id")
    private MetersEntity meter;

}
