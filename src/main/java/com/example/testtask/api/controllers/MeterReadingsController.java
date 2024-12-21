package com.example.testtask.api.controllers;

import com.example.testtask.api.dto.HandbookAddressesDTO;
import com.example.testtask.api.dto.MeterReadingsDTO;
import com.example.testtask.api.exceptions.BadRequestException2;
import com.example.testtask.api.exceptions.NotFoundException2;
import com.example.testtask.api.factories.MeterReadingsDTOFactory;
import com.example.testtask.store.entities.HandbookTypeMetersEntity;
import com.example.testtask.store.entities.MeterReadingsEntity;
import com.example.testtask.store.entities.MetersEntity;
import com.example.testtask.store.repositories.MeterReadingsRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController

//TO DO: Сделать валидацию для обработки ожидаемых форматов данных с помощью библиотеки javax.validation

public class MeterReadingsController {

    MeterReadingsRepository meterReadingsRepository;
    MeterReadingsDTOFactory meterReadingsDTOFactory;

    public static final String CREATE_METER_READINGS = "/api/meterReadings/{meter}/{readingsDate}";
    public static final String EDIT_METER_READINGS = "/api/meterReadings/{meter}/{readingsDate}";
    public static final String GET_ALL_METER_READINGS = "/api/meterReadings/{meter}";
    public static final String GET_METER_READINGS = "/api/meterReadings/{meter}/{readingsDate}";
    public static final String DELETE_METER_READINGS = "/api/meterReadings/{meter}/{readingsDate}";
    public static final String DELETE_ALL_METER_READINGS = "/api/meterReadings/reset";

    @PostMapping(CREATE_METER_READINGS)
    public MeterReadingsDTO createMeterReadingsNumber(
            @PathVariable("meter") MetersEntity meter,
            @PathVariable("readingsDate") Month readingsDate,
            @RequestParam Double readings) {

        meterReadingsRepository
                .findByMeterAndReadingsDate(meter, readingsDate)
                .ifPresent(meterReadings -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Meter: \"%s\" \"%s\" already exists.",
                                    meter, readingsDate
                            )
                    );
                });

        MeterReadingsEntity meterReadings = meterReadingsRepository.saveAndFlush(
                MeterReadingsEntity.builder()
                        .readingsDate(readingsDate)
                        .readings(readings)
                        .updatedAt(Instant.now())
                        .createdAtLocal(LocalDateTime.now(ZoneId.systemDefault()))
                        .build()
        );

        return meterReadingsDTOFactory.makeMeterReadingsDTO(meterReadings);
    }

    @PatchMapping(EDIT_METER_READINGS)
    public MeterReadingsDTO editMeterReadings(
            @PathVariable("meter") MetersEntity meter,
            @PathVariable("readingsDate") Month readingsDate,
            @RequestParam Double readings) {

        MeterReadingsEntity meterReadings = meterReadingsRepository
                .findByMeterAndReadingsDate(meter, readingsDate)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Meter: \"%s\" \"%s\" does not exist.",
                                        meter, readingsDate
                                )
                        )
                );

        meterReadings.setUpdatedAt(Instant.now());

        meterReadings.setReadings(readings);

        meterReadings = meterReadingsRepository.saveAndFlush(meterReadings);

        return meterReadingsDTOFactory.makeMeterReadingsDTO(meterReadings);
    }

    @GetMapping(GET_ALL_METER_READINGS)
    public List<MeterReadingsDTO> getMeterReadings(
            @PathVariable("meter") MetersEntity meter,
            @RequestParam("readings") Long[] sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Sort sort = Sort.by(Arrays.toString(sortBy)).ascending();

        Page<MeterReadingsEntity> allReadings = meterReadingsRepository
                .findAll(PageRequest.of(page, size, sort));

        if (allReadings.isEmpty()) {
            throw new NotFoundException2(
                    "Не найдено данных"
            );
        }

        return allReadings.stream()
                .map(meterReadingsDTOFactory::makeMeterReadingsDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(GET_METER_READINGS)
    public MeterReadingsDTO getMeterReadings(
            @PathVariable("meter") MetersEntity meter,
            @PathVariable("readingsDate") Month readingsDate) {

        MeterReadingsEntity meterReadings = meterReadingsRepository
                .findByMeterAndReadingsDate(meter, readingsDate)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Readings from \"%s\" from Meter: \"%s\" does not exist.",
                                        readingsDate, meter
                                )
                        )
                );

        return meterReadingsDTOFactory.makeMeterReadingsDTO(meterReadings);
    }

    @DeleteMapping(DELETE_METER_READINGS)
    public ResponseEntity<MeterReadingsDTO> deleteMeterReadings(
            @PathVariable("meter") MetersEntity meter,
            @PathVariable("readingsDate") Month readingsDate,
            @RequestParam Long readings) {

        MeterReadingsEntity meterReadings = meterReadingsRepository
                .findByMeterAndReadingsDate(meter, readingsDate)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Readings from \"%s\" from Meter: \"%s\" does not exist.",
                                        readingsDate, meter
                                )
                        )
                );

        meterReadingsRepository.delete(meterReadings);

        System.out.println("Deleted readings data: " + readingsDate + " " + meterReadings.getReadings() + "!");

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(DELETE_ALL_METER_READINGS)
    public ResponseEntity<MeterReadingsDTO> deleteAllReadings() {

        meterReadingsRepository.deleteAll();

        System.out.println("Deleted All meter's readings.");

        return ResponseEntity.noContent().build();
    }
}