package com.example.testtask.api.controllers;

import com.example.testtask.api.dto.MeterReadingsDTO;
import com.example.testtask.api.exceptions.BadRequestException2;
import com.example.testtask.api.exceptions.NotFoundException2;
import com.example.testtask.api.factories.MeterReadingsDTOFactory;
import com.example.testtask.store.entities.MeterReadingsEntity;
import com.example.testtask.store.entities.MetersEntity;
import com.example.testtask.store.repositories.MeterReadingsRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с показаниями приборов учета.
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
public class MeterReadingsController {

    MeterReadingsRepository meterReadingsRepository;
    MeterReadingsDTOFactory meterReadingsDTOFactory;

    public static final String CREATE_METER_READINGS = "/api/meterReadings/{meter}/{readingsDate}";
    public static final String EDIT_METER_READINGS = "/api/meterReadings/{meter}/{readingsDate}";
    public static final String GET_ALL_METER_READINGS = "/api/meterReadings/{meter}";
    public static final String GET_METER_READINGS = "/api/meterReadings/{meter}/{readingsDate}";
    public static final String DELETE_METER_READINGS = "/api/meterReadings/{meter}/{readingsDate}";
    public static final String DELETE_ALL_METER_READINGS = "/api/meterReadings/reset";

    /**
     * Создает новые показания для прибора учета.
     *
     * @param meter прибор учета
     * @param readingsDate дата показаний
     * @param readings показания
     * @return созданные показания в виде DTO
     * @throws BadRequestException2 если показания уже существуют
     */
    @PostMapping(CREATE_METER_READINGS)
    public MeterReadingsDTO createMeterReadingsNumber(
            @Valid
            @PathVariable("meter") MetersEntity meter,
            @PathVariable("readingsDate") Month readingsDate,
            @RequestParam BigDecimal readings) {

        meterReadingsRepository
                .findByMeterAndReadingsDate(meter, readingsDate)
                .ifPresent(meterReadings -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Показания прибора \"%s\" за период \"%s\" уже существуют.",
                                    meter, readingsDate
                            )
                    );
                });

        BigDecimal roundedReadings = readings.setScale(3, RoundingMode.HALF_UP);

        MeterReadingsEntity meterReadings = meterReadingsRepository.saveAndFlush(
                MeterReadingsEntity.builder()
                        .readingsDate(readingsDate)
                        .readings(roundedReadings) // Используем округленное значение
                        .updatedAt(Instant.now())
                        .createdAtLocal(LocalDateTime.now(ZoneId.systemDefault()))
                        .build()
        );

        return meterReadingsDTOFactory.makeMeterReadingsDTO(meterReadings);
    }

    /**
     * Обновляет существующие показания прибора учета.
     *
     * @param meter прибор учета
     * @param readingsDate дата показаний
     * @param readings новые показания
     * @return обновленные показания в виде DTO
     * @throws NotFoundException2 если показания не найдены
     */
    @PatchMapping(EDIT_METER_READINGS)
    public MeterReadingsDTO editMeterReadings(
            @Valid
            @PathVariable("meter") MetersEntity meter,
            @PathVariable("readingsDate") Month readingsDate,
            @RequestParam BigDecimal readings) {

        MeterReadingsEntity meterReadings = meterReadingsRepository
                .findByMeterAndReadingsDate(meter, readingsDate)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Показания прибора \"%s\" за период \"%s\" не существуют.",
                                        meter, readingsDate
                                )
                        )
                );

        meterReadings.setUpdatedAt(Instant.now());
        meterReadings.setReadings(readings);

        meterReadings = meterReadingsRepository.saveAndFlush(meterReadings);

        return meterReadingsDTOFactory.makeMeterReadingsDTO(meterReadings);
    }

    /**
     * Получает все показания для прибора учета с пагинацией.
     *
     * @param meter прибор учета
     * @param sortBy поля для сортировки
     * @param page номер страницы
     * @param size размер страницы
     * @return список показаний в виде DTO
     * @throws NotFoundException2 если показания не найдены
     */
    @GetMapping(GET_ALL_METER_READINGS)
    public List<MeterReadingsDTO> getMeterReadings(
            @Valid
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

    /**
     * Получает показания прибора учета по дате.
     *
     * @param meter прибор учета
     * @param readingsDate дата показаний
     * @return показания в виде DTO
     * @throws NotFoundException2 если показания не найдены
     */
    @GetMapping(GET_METER_READINGS)
    public MeterReadingsDTO getMeterReadings(
            @Valid
            @PathVariable("meter") MetersEntity meter,
            @PathVariable("readingsDate") Month readingsDate) {

        MeterReadingsEntity meterReadings = meterReadingsRepository
                .findByMeterAndReadingsDate(meter, readingsDate)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Показания прибора \"%s\" за период \"%s\" не существуют",
                                        meter, readingsDate
                                )
                        )
                );

        return meterReadingsDTOFactory.makeMeterReadingsDTO(meterReadings);
    }

    /**
     * Удаляет показания прибора учета по дате.
     *
     * @param meter прибор учета
     * @param readingsDate дата показаний
     * @param readings показания
     * @return ответ без содержимого
     * @throws NotFoundException2 если показания не найдены
     */
    @DeleteMapping(DELETE_METER_READINGS)
    public ResponseEntity<MeterReadingsDTO> deleteMeterReadings(
            @Valid
            @PathVariable("meter") MetersEntity meter,
            @PathVariable("readingsDate") Month readingsDate,
            @RequestParam BigDecimal readings) {

        MeterReadingsEntity meterReadings = meterReadingsRepository
                .findByMeterAndReadingsDate(meter, readingsDate)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Показания прибора \"%s\" за период \"%s\" не существуют.",
                                        meter, readingsDate
                                )
                        )
                );

        meterReadingsRepository.delete(meterReadings);

        System.out.println("Удалены показания за: " + readingsDate + " с значением: " + meterReadings.getReadings() + "!");

        return ResponseEntity.noContent().build();
    }

    /**
     * Удаляет все показания приборов учета.
     *
     * @return ответ без содержимого
     */
    @DeleteMapping(DELETE_ALL_METER_READINGS)
    public ResponseEntity<MeterReadingsDTO> deleteAllReadings() {

        meterReadingsRepository.deleteAll();

        System.out.println("Удалены все показания!");

        return ResponseEntity.noContent().build();
    }
}