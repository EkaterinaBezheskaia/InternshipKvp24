package com.example.testtask.store.entities;

import com.example.testtask.api.dto.HandbookAddressesDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * Сущность для представления адресов в справочнике.
 * Наследует от DTO для передачи данных.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "handbook_adresses")
public class HandbookAddressesEntity extends HandbookAddressesDTO {

    /**
     * Уникальный идентификатор адреса.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "column_0_id")
    private Long id;

    /**
     * Название улицы.
     */
    @Column(name = "column_1_street", nullable = false)
    private String street;

    /**
     * Номер дома.
     */
    @Column(name = "column_2_number", nullable = false)
    private int number;

    /**
     * Литерал адреса (необязательный).
     */
    @Column(name = "column_3_literal")
    private String literal;

    /**
     * Номер квартиры (необязательный).
     */
    @Column(name = "column_4_flat")
    private int flat;

    /**
     * Дата и время создания адреса.
     */
    @Column(name = "column_5_utc_date", nullable = false)
    private Instant createdAt = Instant.now();

    /**
     * Локальное время создания адреса.
     */
    @Column(name = "column_6_located_date", nullable = false)
    private LocalDateTime createdAtLocal = LocalDateTime.now(ZoneId.systemDefault());

    /**
     * Дата и время последнего обновления адреса.
     */
    @Column(name = "column_7_updated_date")
    Instant updatedAt = Instant.now();

    /**
     * Список приборов учета, связанных с данным адресом.
     */
    @OneToMany(mappedBy = "address")
    private List<MetersEntity> meters;
}