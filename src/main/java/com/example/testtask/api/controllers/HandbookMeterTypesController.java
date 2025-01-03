package com.example.testtask.api.controllers;

import com.example.testtask.api.dto.HandbookMeterTypesDTO;
import com.example.testtask.api.exceptions.BadRequestException2;
import com.example.testtask.api.exceptions.NotFoundException2;
import com.example.testtask.api.factories.HandbookMeterTypesDTOFactory;
import com.example.testtask.store.entities.HandbookMeterTypesEntity;
import com.example.testtask.store.repositories.HandbookMeterTypesRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
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
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с типами приборов учета.
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
public class HandbookMeterTypesController {

    HandbookMeterTypesRepository handbookMeterTypesRepository;
    HandbookMeterTypesDTOFactory handbookMeterTypesDTOFactory;

    public static final String CREATE_METER_TYPE = "/api/meterTypes";
    public static final String EDIT_METER_TYPE = "/api/meterTypes/{meterTypeTitle}";
    public static final String GET_ALL_METER_TYPES = "/api/meterTypes";
    public static final String GET_METER_TYPE = "/api/meterTypes/{meterTypeTitle}";
    public static final String DELETE_METER_TYPE = "/api/meterTypes/{meterTypeTitle}";
    public static final String DELETE_ALL_METER_TYPES = "/api/meterTypes/reset";

    /**
     * Создает новый тип прибора учета.
     *
     * @param meterTypeTitle название типа прибора учета
     * @return созданный тип прибора учета в виде DTO
     * @throws BadRequestException2 если тип уже существует, если meterTypeTitle не удовлетворяет условиям
     */
    @PostMapping(CREATE_METER_TYPE)
    public HandbookMeterTypesDTO createMeterType(
            @Valid
            @RequestParam String meterTypeTitle) {

        if (meterTypeTitle == null || meterTypeTitle.trim().isEmpty()) {
            throw new BadRequestException2("Параметр 'meterTypeTitle' не должен быть пустым.");
        }

        handbookMeterTypesRepository
                .findByMeterTypeTitle(meterTypeTitle)
                .ifPresent(existingType -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Тип приборов учета \"%s\" уже существует.",
                                    meterTypeTitle
                            )
                    );
                });

        HandbookMeterTypesEntity meterType = handbookMeterTypesRepository.saveAndFlush(
                HandbookMeterTypesEntity.builder()
                        .meterTypeTitle(meterTypeTitle)
                        .createdAt(Instant.now())
                        .createdAtLocal(LocalDateTime.now(ZoneId.systemDefault()))
                        .build()
        );

        return handbookMeterTypesDTOFactory.makeHandbookMeterTypesDTO(meterType);
    }

    /**
     * Обновляет существующий тип прибора учета.
     *
     * @param meterTypeTitle старое название типа
     * @param newMeterTypeTitle новое название типа
     * @return обновленный тип прибора учета в виде DTO
     * @throws NotFoundException2 если тип не найден
     * @throws BadRequestException2 если новый тип уже существует, если meterTypeTitle не удовлетворяет условиям
     */
    @PatchMapping(EDIT_METER_TYPE)
    public HandbookMeterTypesDTO editMeterType(
            @Valid
            @PathVariable("meterTypeTitle") String meterTypeTitle,
            @RequestParam String newMeterTypeTitle) {

        if (meterTypeTitle == null || meterTypeTitle.trim().isEmpty()) {
            throw new BadRequestException2("Параметр 'meterTypeTitle' не должен быть пустым.");
        }

        HandbookMeterTypesEntity meterType = handbookMeterTypesRepository
                .findByMeterTypeTitle(meterTypeTitle)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Тип приборов учета \"%s\" не существует.",
                                        meterTypeTitle
                                )
                        )
                );

        handbookMeterTypesRepository
                .findByMeterTypeTitle(newMeterTypeTitle)
                .ifPresent(existingType -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Тип приборов учета \"%s\" уже существует.",
                                    newMeterTypeTitle
                            )
                    );
                });

        meterType.setUpdatedAt(Instant.now());
        meterType.setMeterTypeTitle(newMeterTypeTitle);

        meterType = handbookMeterTypesRepository.saveAndFlush(meterType);

        return handbookMeterTypesDTOFactory.makeHandbookMeterTypesDTO(meterType);
    }

    /**
     * Получает все типы приборов учета с пагинацией и сортировкой.
     *
     * @param page номер страницы
     * @param size размер страницы
     * @param sortBy поле для сортировки
     * @return список типов приборов учета в виде DTO
     * @throws NotFoundException2 если типы не найдены
     */
    @GetMapping(GET_ALL_METER_TYPES)
    public List<HandbookMeterTypesDTO> getAllMeterTypes(
            @Valid
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "meterTypeTitle") String sortBy) {

        Sort sort = Sort.by(sortBy).ascending();

        Page<HandbookMeterTypesEntity> allMeterTypes = handbookMeterTypesRepository
                .findAll(PageRequest.of(page, size, sort));

        if (allMeterTypes.isEmpty()) {
            throw new NotFoundException2(
                    "Не найдено типов приборов учета"
            );
        }

        return allMeterTypes.stream()
                .map(handbookMeterTypesDTOFactory::makeHandbookMeterTypesDTO)
                .collect(Collectors.toList());
    }

    /**
     ```java
     * Получает тип прибора учета по названию.
     *
     * @param meterTypeTitle название типа прибора учета
     * @return тип прибора учета в виде DTO
     * @throws NotFoundException2 если тип не найден
     */
    @GetMapping(GET_METER_TYPE)
    public HandbookMeterTypesDTO getMeterType(
            @Valid
            @PathVariable("meterTypeTitle") String meterTypeTitle) {

        HandbookMeterTypesEntity meterType = handbookMeterTypesRepository
                .findByMeterTypeTitle(meterTypeTitle)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Тип приборов учета \"%s\" не существует.",
                                        meterTypeTitle
                                )
                        )
                );

        System.out.println("Найден тип приборов учета: " + meterType.getMeterTypeTitle());

        return handbookMeterTypesDTOFactory.makeHandbookMeterTypesDTO(meterType);
    }

    /**
     * Удаляет тип прибора учета по названию.
     *
     * @param meterTypeTitle название типа прибора учета
     * @return ответ без содержимого
     * @throws NotFoundException2 если тип не найден
     */
    @DeleteMapping(DELETE_METER_TYPE)
    public ResponseEntity<HandbookMeterTypesDTO> deleteMeterType(
            @Valid
            @PathVariable("meterTypeTitle") String meterTypeTitle) {

        HandbookMeterTypesEntity meterType = handbookMeterTypesRepository
                .findByMeterTypeTitle(meterTypeTitle)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Тип приборов учета \"%s\" не существует.",
                                        meterTypeTitle
                                )
                        )
                );

        handbookMeterTypesRepository.delete(meterType);

        System.out.println("Удален тип приборов учета: " + meterType.getMeterTypeTitle());

        return ResponseEntity.noContent().build();
    }

    /**
     * Удаляет все типы приборов учета.
     *
     * @return ответ без содержимого
     */
    @DeleteMapping(DELETE_ALL_METER_TYPES)
    public ResponseEntity<HandbookMeterTypesDTO> deleteAllMeterTypes() {

        handbookMeterTypesRepository.deleteAll();

        System.out.println("Удалены все типы приборов учета!");

        return ResponseEntity.noContent().build();
    }
}