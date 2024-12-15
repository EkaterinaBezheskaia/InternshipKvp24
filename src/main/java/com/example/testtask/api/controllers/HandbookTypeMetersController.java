package com.example.testtask.api.controllers;

import com.example.testtask.api.dto.HandbookTypeMetersDTO;
import com.example.testtask.api.exceptions.BadRequestException2;
import com.example.testtask.api.exceptions.NotFoundException2;
import com.example.testtask.api.factories.HandbookTypeMetersDTOFactory;
import com.example.testtask.store.entities.HandbookTypeMetersEntity;
import com.example.testtask.store.repositories.HandbookTypeMetersRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController

//TO DO: Сделать валидацию для обработки ожидаемых форматов данных с помощью библиотеки javax.validation

public class HandbookTypeMetersController {

    HandbookTypeMetersRepository handbookTypeMetersRepository;
    HandbookTypeMetersDTOFactory handbookTypeMetersDTOFactory;

    public static final String CREATE_TYPE_METER = "/api/typeMeters";
    public static final String EDIT_TYPE_METER = "/api/typeMeters/{titleTypeMeters}";
    public static final String GET_TYPE_METER = "/api/typeMeters/{titleTypeMeters}";
    public static final String GET_ALL_TYPE_METERS = "/api/typeMeters";
    public static final String DELETE_TYPE_METER = "/api/typeMeters/{titleTypeMeters}";

    @PostMapping(CREATE_TYPE_METER)
    public HandbookTypeMetersDTO createTypeMeter(
            @RequestParam String titleTypeMeters) {

        handbookTypeMetersRepository
                .findByTitleTypeMeters(titleTypeMeters)
                .ifPresent(handbookTypeMeters -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Type Meter: \"%s\" already exists.",
                                    titleTypeMeters
                            )
                    );
                });

        HandbookTypeMetersEntity handbookTypeMeters = handbookTypeMetersRepository.saveAndFlush(
                HandbookTypeMetersEntity.builder()
                        .titleTypeMeters(titleTypeMeters)
                        .createdAt(Instant.now())
                        .build()
        );

        return handbookTypeMetersDTOFactory.makeHandbookTypeMetersDTO(handbookTypeMeters);
    }

    @PatchMapping(EDIT_TYPE_METER)
    public HandbookTypeMetersDTO editTypeMeter(
            @PathVariable("titleTypeMeters") String titleTypeMeters,
            @RequestParam String newTitleTypeMeters) {

        HandbookTypeMetersEntity handbookTypeMeters = handbookTypeMetersRepository
                .findByTitleTypeMeters(titleTypeMeters)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Type Meter: \"%s\" does not exist.",
                                        titleTypeMeters
                                )
                        )
                );

        handbookTypeMetersRepository
                .findByTitleTypeMeters(newTitleTypeMeters)
                .ifPresent(existingTypeMeters -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Type Meter: \"%s\" already exists.",
                                    newTitleTypeMeters
                            )
                    );
                });

        handbookTypeMeters.setTitleTypeMeters(newTitleTypeMeters);

        handbookTypeMeters = handbookTypeMetersRepository.saveAndFlush(handbookTypeMeters);

        return handbookTypeMetersDTOFactory.makeHandbookTypeMetersDTO(handbookTypeMeters);
    }

    @GetMapping(HandbookTypeMetersController.GET_TYPE_METER)
    public HandbookTypeMetersDTO getTitleAddress(
            @PathVariable("titleTypeMeters") String titleTypeMeters) {

        HandbookTypeMetersEntity handbookTypeMeters = handbookTypeMetersRepository
                .findByTitleTypeMeters(titleTypeMeters)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Type Meter: \"%s\" does not exist.",
                                        titleTypeMeters
                                )
                        )
                );

        System.out.println("Found Type Meter: " + handbookTypeMeters.getTitleTypeMeters());

        return handbookTypeMetersDTOFactory.makeHandbookTypeMetersDTO(handbookTypeMeters);
    }

    @GetMapping(GET_ALL_TYPE_METERS)
    public List<HandbookTypeMetersDTO> getAllTypeMeters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "titleTypeMeters") String sortBy) {

        Sort sort = Sort.by(sortBy).ascending();

        Page<HandbookTypeMetersEntity> allTypeMeters = handbookTypeMetersRepository
                .findAll(PageRequest.of(page, size, sort));

        return allTypeMeters.stream()
                .map(handbookTypeMetersDTOFactory::makeHandbookTypeMetersDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping(DELETE_TYPE_METER)
    public ResponseEntity<HandbookTypeMetersDTO> deleteTypeMeters(
            @PathVariable("titleTypeMeters") String titleTypeMeters) {

        HandbookTypeMetersEntity handbookTypeMeters = handbookTypeMetersRepository
                .findByTitleTypeMeters(titleTypeMeters)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Type Meter: \"%s\" does not exist.",
                                        titleTypeMeters
                                )
                        )
                );

        handbookTypeMetersRepository.delete(handbookTypeMeters);

        System.out.println("Deleted Type Meter: " + handbookTypeMeters.getTitleTypeMeters());

        return ResponseEntity.noContent().build();
    }
}