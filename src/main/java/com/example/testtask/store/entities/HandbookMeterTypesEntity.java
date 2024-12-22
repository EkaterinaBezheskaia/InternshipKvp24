package com.example.testtask.store.entities;

import com.example.testtask.api.dto.HandbookMeterTypesDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.time.ZoneId;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "handbook_meter_types")

public class HandbookMeterTypesEntity extends HandbookMeterTypesDTO {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column (name = "column_0_id")
        private Long id;

        @Column (name = "column_1_title_type_meters", unique = true)
        private String meterTypeTitle;

        @Builder.Default
        @Column (name = "column_2_utc_date")
        Instant createdAt = Instant.now();

        @Column (name = "column_3_located_date", nullable = false)
        private LocalDateTime createdAtLocal = LocalDateTime.now(ZoneId.systemDefault());

        @Column (name = "column_4_updated_date")
        Instant updatedAt = Instant.now();

        @OneToMany(mappedBy = "type")
        private List<MetersEntity> meters;

}
