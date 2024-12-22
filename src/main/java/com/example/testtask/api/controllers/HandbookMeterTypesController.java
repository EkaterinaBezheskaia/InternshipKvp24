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

    @PostMapping(CREATE_METER_TYPE)
    public HandbookMeterTypesDTO createMeterType(
            @Valid
            @RequestParam String meterTypeTitle) {

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

    @PatchMapping(EDIT_METER_TYPE)
    public HandbookMeterTypesDTO editMeterType(
            @PathVariable("meterTypeTitle") String meterTypeTitle,
            @RequestParam
            @Size(max = 100, message = "Название типа прибора не должно превышать 100 символов")
            String newMeterTypeTitle) {

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

    @GetMapping(GET_ALL_METER_TYPES)
    public List<HandbookMeterTypesDTO> getAllMeterTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy) {

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

    @ GetMapping(GET_METER_TYPE)
    public HandbookMeterTypesDTO getMeterType(
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

    @DeleteMapping(DELETE_METER_TYPE)
    public ResponseEntity<HandbookMeterTypesDTO> deleteMeterType(
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

    @DeleteMapping(DELETE_ALL_METER_TYPES)
    public ResponseEntity<HandbookMeterTypesDTO> deleteAllMeterTypes() {

        handbookMeterTypesRepository.deleteAll();

        System.out.println("Удалены все типы приборов учета!");

        return ResponseEntity.noContent().build();
    }
}