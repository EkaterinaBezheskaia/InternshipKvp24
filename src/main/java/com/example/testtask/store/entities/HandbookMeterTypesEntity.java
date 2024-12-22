package com.example.testtask.store.entities;

import com.example.testtask.api.dto.HandbookMeterTypesDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.time.ZoneId;

/**
 * Сущность для представления типов приборов учета.
 * Наследует от DTO для передачи данных.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "handbook_meter_types")
public class HandbookMeterTypesEntity extends HandbookMeterTypesDTO {

        /**
         * Уникальный идентификатор типа прибора учета.
         */
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "column_0_id")
        private Long id;

        /**
         * Название типа прибора учета.
         * Должно быть уникальным.
         */
        @Column(name = "column_1_title_type_meters", unique = true)
        private String meterTypeTitle;

        /**
         * Дата и время создания типа прибора учета.
         * Устанавливается по умолчанию на текущее время.
         */
        @Builder.Default
        @Column(name = "column_2_utc_date")
        Instant createdAt = Instant.now();

        /**
         * Локальное время создания типа прибора учета.
         * Не может быть пустым.
         */
        @Column(name = "column_3_located_date", nullable = false)
        private LocalDateTime createdAtLocal = LocalDateTime.now(ZoneId.systemDefault());

        /**
         * Дата и время последнего обновления типа прибора учета.
         */
        @Column(name = "column_4_updated_date")
        Instant updatedAt = Instant.now();

        /**
         * Список приборов учета, связанных с данным типом.
         */
        @OneToMany(mappedBy = "type")
        private List<MetersEntity> meters;
}
