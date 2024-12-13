package com.example.testtask.api.controllers;

import com.example.testtask.api.dto.MetersDTO;
import com.example.testtask.api.exceptions.BadRequestException2;
import com.example.testtask.api.exceptions.NotFoundException2;
import com.example.testtask.api.factories.MetersDTOFactory;
import com.example.testtask.store.entities.HandbookAddressesEntity;
import com.example.testtask.store.entities.MetersEntity;
import com.example.testtask.store.repositories.HandbookAddressesRepository;
import com.example.testtask.store.repositories.MetersRepository;
import com.example.testtask.api.controllers.HandbookAddressesController;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController

//TO DO: Сделать валидацию для обработки ожидаемых форматов данных с помощью библиотеки javax.validation

public class MetersController {

    MetersRepository metersRepository;
    MetersDTOFactory metersDTOFactory;

    public static final String CREATE_METERS = "/api/meters";
    public static final String EDIT_METERS = "/api/meters/{titleMetersNumber}";
    public static final String GET_METERS = "/api/meters/{titleMetersNumber}";
    public static final String DELETE_METERS = "/api/meters/{titleMetersNumber}";
    private final HandbookAddressesRepository handbookAddressesRepository;

    @PostMapping(CREATE_METERS)
    public MetersDTO createMetersNumber(
            @RequestParam String titleMetersNumber,
            @RequestParam String street,
            @RequestParam Integer number,
            @RequestParam(required=false) String literal,
            @RequestParam(required=false) Integer flat) {

        metersRepository
                .findByTitleMetersNumber(titleMetersNumber)
                .ifPresent(meters -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Meter: \"%s\" already exists.",
                                    titleMetersNumber
                            )
                    );
                });

        HandbookAddressesEntity address = handbookAddressesRepository
                .findByStreetAndNumberAndLiteralAndFlat(street, number, literal, flat)
                .orElseThrow(() -> new NotFoundException2(
                        String.format(
                                "Address \"%s %d %s %d\" does not exist.",
                                street, number, literal, flat
                        )
                ));

        MetersEntity meters = metersRepository.saveAndFlush(
                MetersEntity.builder()
                        .titleMetersNumber(titleMetersNumber)
                        .address(address)
                        .build()
        );

        return metersDTOFactory.makeMetersDTO(meters);
    }

    @PatchMapping(EDIT_METERS)
    public MetersDTO editMeter(
            @PathVariable("titleMetersNumber") String titleMetersNumber,
            @RequestParam String newTitleMetersNumber) {

        MetersEntity meters = metersRepository
                .findByTitleMetersNumber(titleMetersNumber)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Meter: \"%s\" does not exist.",
                                        titleMetersNumber
                                )
                        )
                );

        metersRepository
                .findByTitleMetersNumber(newTitleMetersNumber)
                .ifPresent(existingMeters -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Meter: \"%s\" already exists.",
                                    titleMetersNumber
                            )
                    );
                });

        meters.setTitleMetersNumber(newTitleMetersNumber);

        meters = metersRepository.saveAndFlush(meters);

        return metersDTOFactory.makeMetersDTO(meters);
    }

    @GetMapping(GET_METERS)
    public MetersDTO getTitleMeters(
            @PathVariable("titleMetersNumber") String titleMetersNumber) {

        MetersEntity meters = metersRepository
                .findByTitleMetersNumber(titleMetersNumber)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Meter: \"%s\" does not exist.",
                                        titleMetersNumber
                                )
                        )
                );

        System.out.println("Found Meter: " + meters.getTitleMetersNumber());

        return metersDTOFactory.makeMetersDTO(meters);
    }

    @DeleteMapping(DELETE_METERS)
    public ResponseEntity<MetersDTO> deleteMeters(
            @PathVariable("titleMetersNumber") String titleMetersNumber) {

        MetersEntity meters = metersRepository
                .findByTitleMetersNumber(titleMetersNumber)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Meter: \"%s\" does not exist.",
                                        titleMetersNumber
                                )
                        )
                );

        metersRepository.delete(meters);

        System.out.println("Deleted Meter: " + meters.getTitleMetersNumber());

        return ResponseEntity.noContent().build();
    }
}