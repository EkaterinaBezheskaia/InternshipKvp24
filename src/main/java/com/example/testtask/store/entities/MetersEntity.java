package com.example.testtask.store.entities;

import com.example.testtask.api.dto.MetersDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.time.ZoneId;

/**
 * Сущность для представления приборов учета.
 * Наследует от DTO для передачи данных.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "meters")
public class MetersEntity extends MetersDTO {

    /**
     * Уникальный идентификатор прибора учета.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "column_0_id")
    private Long id;

    /**
     * Серийный номер прибора учета.
     * Должен быть уникальным.
     */
    @Column(name = "column_1_serial_number", unique = true)
    private String metersSerialNumber;

    /**
     * Дата установки прибора учета.
     */
    @Column(name = "column_2_installation_date")
    private LocalDate installationDate;

    /**
     * Дата и время создания записи о приборе учета.
     * Устанавливается по умолчанию на текущее время.
     */
    @Builder.Default
    @Column(name = "column_4_utc_date")
    Instant createdAt = Instant.now();

    /**
     * Локальное время создания записи о приборе учета.
     * Не может быть пустым.
     */
    @Column(name = "column_5_located_date", nullable = false)
    private LocalDateTime createdAtLocal = LocalDateTime.now(ZoneId.systemDefault());

    /**
     * Дата и время последнего обновления записи о приборе учета.
     */
    @Column(name = "column_6_updated_date")
    Instant updatedAt = Instant.now();

    /**
     * Адрес, к которому относится прибор учета.
     */
    @ManyToOne
    @JoinColumn(name = "column_7_address_id")
    private HandbookAddressesEntity address;

    /**
     * Тип прибора учета.
     */
    @ManyToOne
    @JoinColumn(name = "column_8_type_id")
    private HandbookMeterTypesEntity type;

    /**
     * Список показаний, связанных с данным прибором учета.
     */
    @OneToMany(mappedBy = "meter")
    private List<MeterReadingsEntity> readings;
}
