package com.example.testtask.store.entities;

import com.example.testtask.api.dto.HandbookTypeMetersDTO;
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
//@Table(name = "HandbookTypeMeters")

public class HandbookTypeMetersEntity extends HandbookTypeMetersDTO {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column //(name = "column_0_id")
        private Long id;

        @Column //(name = "column_1_titleTypeMeters", unique = true)
        private String titleTypeMeters;

        @Builder.Default
        @Column //(name = "column_2_creation_date")
        Instant createdAt = Instant.now();

        @OneToMany(mappedBy = "type")
        private List<MetersEntity> meters;

}
