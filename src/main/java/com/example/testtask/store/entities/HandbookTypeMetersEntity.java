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
//@Table(name = "files_base")
public class HandbookTypeMetersEntity extends HandbookTypeMetersDTO {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true)
        private String titleTypeMeters;

//        @Builder.Default
//        String base64File = "";

        @Builder.Default
        Instant creationDate = Instant.now();

        @Builder.Default
        private String description = "";

        @OneToMany(mappedBy = "type")
        private List<MetersEntity> meters;

}
