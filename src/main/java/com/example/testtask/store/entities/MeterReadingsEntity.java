package com.example.testtask.store.entities;

import com.example.testtask.api.dto.MeterReadingsDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

/**
 * Сущность для представления показаний приборов учета.
 * Наследует от DTO для передачи данных.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "meter_readings")
public class MeterReadingsEntity extends MeterReadingsDTO {

    /**
     * Уникальный идентификатор показаний.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "column_0_id")
    private Long id;

    /**
     * Дата показаний, представлена в виде месяца.
     * Не может быть пустым.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "column_1_readings_date", nullable = false)
    private Month readingsDate;

    /**
     * Значение показаний прибора.
     * Не может быть пустым.
     */
    @Column(name = "column_2_readins", nullable = false)
    private BigDecimal readings;

    /**
     * Дата и время создания записи о показаниях.
     * Устанавливается по умолчанию на текущее время.
     */
    @Builder.Default
    @Column(name = "column_3_utc_date")
    Instant creationDate = Instant.now();

    /**
     * Локальное время создания записи о показаниях.
     * Не может быть пустым.
     */
    @Column(name = "column_4_located_date", nullable = false)
    private LocalDateTime createdAtLocal = LocalDateTime.now(ZoneId.systemDefault());

    /**
     * Дата и время последнего обновления записи о показаниях.
     */
    @Column(name = "column_5_updated_date")
    Instant updatedAt = Instant.now();

    /**
     * Прибор учета, к которому относятся показания.
     */
    @ManyToOne
    @JoinColumn(name = "column_6_meter_id")
    private MetersEntity meter;
}
