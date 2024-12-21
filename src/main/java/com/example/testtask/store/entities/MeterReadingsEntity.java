package com.example.testtask.store.entities;

import com.example.testtask.api.dto.MeterReadingsDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "meter_readings")

public class MeterReadingsEntity extends MeterReadingsDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "column_0_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "column_1_readings_date", nullable = false)
    private Month readingsDate;

    @Column (name = "column_2_readins", nullable = false)
    private Double readings;

    @Builder.Default
    @Column (name = "column_3_creation_date")
    Instant creationDate = Instant.now();

    @Column (name = "column_4_UTC_date", nullable = false)
    private LocalDateTime createdAtLocal = LocalDateTime.now(ZoneId.systemDefault());

    @Column (name = "column_5_updated_date")
    Instant updatedAt = Instant.now();

    @ManyToOne
    @JoinColumn (name = "column_6_meter_id")
    private MetersEntity meter;

}
