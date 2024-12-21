package com.example.testtask.store.entities;

import com.example.testtask.api.dto.HandbookTypeMetersDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.time.ZoneId;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "handbook_type_meters")

public class HandbookTypeMetersEntity extends HandbookTypeMetersDTO {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column (name = "column_0_id")
        private Long id;

        @Column (name = "column_1_title_type_meters", unique = true)
        private String titleTypeMeters;

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
