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
//@Table(name = "Readings")

public class MeterReadingsEntity extends MeterReadingsDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column //(name = "column_0_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Month readingsDate;

    @Builder.Default
    @Column //(name = "column_2_creation_date")
    Instant createdAt = Instant.now();

    @Column (nullable = false)
    private Long readings;

    @ManyToOne
    @JoinColumn //(name = "meter_id")
    private MetersEntity meter;

}
