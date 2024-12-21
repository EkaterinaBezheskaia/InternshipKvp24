package com.example.testtask.store.entities;

import com.example.testtask.api.dto.MetersDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
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
@Table(name = "meters")

public class MetersEntity extends MetersDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "column_0_id")
    private Long id;

    @Column (name = "column_1_serial_number", unique = true)
    private String metersSerialNumber;

    @Column (name = "column_2_installation_date")
    private LocalDate installationDate;

    @Builder.Default
    @Column (name = "column_4_creation_date")
    Instant creationDate = Instant.now();

    @Column (name = "column_5_utc_date", nullable = false)
    private LocalDateTime createdAtLocal = LocalDateTime.now(ZoneId.systemDefault());

    @ManyToOne
    @JoinColumn (name = "column_6_address_id")
    private HandbookAddressesEntity address;

    @ManyToOne
    @JoinColumn (name = "column_7_type_id")
    private HandbookTypeMetersEntity type;

    @OneToMany(mappedBy = "meter")
    private List<MeterReadingsEntity> readings;

}
