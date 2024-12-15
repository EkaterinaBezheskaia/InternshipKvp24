package com.example.testtask.api.controllers;

import com.example.testtask.api.dto.MetersDTO;
import com.example.testtask.api.exceptions.BadRequestException2;
import com.example.testtask.api.exceptions.NotFoundException2;
import com.example.testtask.api.factories.MetersDTOFactory;
import com.example.testtask.store.entities.HandbookAddressesEntity;
import com.example.testtask.store.entities.MetersEntity;
import com.example.testtask.store.repositories.HandbookAddressesRepository;
import com.example.testtask.store.repositories.MetersRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController

//TO DO: Сделать валидацию для обработки ожидаемых форматов данных с помощью библиотеки javax.validation

public class MetersController {

    MetersRepository metersRepository;
    MetersDTOFactory metersDTOFactory;
    HandbookAddressesRepository handbookAddressesRepository;

    public static final String CREATE_METERS = "/api/meters";
    public static final String EDIT_METERS = "/api/meters/{metersSerialNumber}";
    public static final String EDIT_DATE = "/api/meters/{metersSerialNumber}/{installationDate}";
    public static final String GET_METERS = "/api/meters/{metersSerialNumber}";
    public static final String DELETE_METERS = "/api/meters/{metersSerialNumber}";

    @PostMapping(CREATE_METERS)
    public MetersDTO createMetersSerialNumber(
            @RequestParam String metersSerialNumber,
            @RequestParam String street,
            @RequestParam Integer number,
            @RequestParam(required=false) String literal,
            @RequestParam(required=false) Integer flat,
            @RequestParam LocalDate installationDate) {

        metersRepository
                .findByMetersSerialNumber(metersSerialNumber)
                .ifPresent(meters -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Meter: \"%s\" already exists.",
                                    metersSerialNumber
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
                        .metersSerialNumber(metersSerialNumber)
                        .address(address)
                        .installationDate(installationDate)
                        .build()
        );

        return metersDTOFactory.makeMetersDTO(meters);
    }

    @PatchMapping(EDIT_METERS)
    public MetersDTO editMeter(
            @PathVariable("metersSerialNumber") String metersSerialNumber,
            @RequestParam String newMetersSerialNumber) {

        MetersEntity meters = metersRepository
                .findByMetersSerialNumber(metersSerialNumber)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Meter: \"%s\" does not exist.",
                                        metersSerialNumber
                                )
                        )
                );

        metersRepository
                .findByMetersSerialNumber(metersSerialNumber)
                .ifPresent(existingMeters -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Meter: \"%s\" already exists.",
                                    newMetersSerialNumber
                            )
                    );
                });

        meters.setMetersSerialNumber(newMetersSerialNumber);

        meters = metersRepository.saveAndFlush(meters);

        return metersDTOFactory.makeMetersDTO(meters);
    }

    @PatchMapping(EDIT_DATE)
    public MetersDTO editDate(
            @PathVariable("metersSerialNumber") String metersSerialNumber,
            @PathVariable("installationDate") LocalDate installationDate,
            @RequestParam LocalDate newInstallationDate) {

        MetersEntity meters = metersRepository
                .findByMetersSerialNumber(metersSerialNumber)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Meter: \"%s\" does not exist.",
                                        metersSerialNumber
                                )
                        )
                );

        MetersEntity meters_installation_date = metersRepository
                .findByMetersSerialNumberAndInstallationDate(metersSerialNumber, newInstallationDate)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Meter: \"%s\" \"%s\" does not exist.",
                                        metersSerialNumber, installationDate
                                )
                        )
                );

        metersRepository
                .findByMetersSerialNumberAndInstallationDate(metersSerialNumber, newInstallationDate)
                .ifPresent(existingMeters -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Meter: \"%s\" \"%s\" already exists.",
                                    metersSerialNumber, newInstallationDate
                            )
                    );
                });

        meters.setInstallationDate(newInstallationDate);

        meters = metersRepository.saveAndFlush(meters);

        return metersDTOFactory.makeMetersDTO(meters);
    }

    @GetMapping(GET_METERS)
    public MetersDTO getTitleMeters(
            @PathVariable("metersSerialNumber") String metersSerialNumber) {

        MetersEntity meters = metersRepository
                .findByMetersSerialNumber(metersSerialNumber)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Meter: \"%s\" does not exist.",
                                        metersSerialNumber
                                )
                        )
                );

        System.out.println("Found Meter: " + meters.getMetersSerialNumber());

        return metersDTOFactory.makeMetersDTO(meters);
    }

    @DeleteMapping(DELETE_METERS)
    public ResponseEntity<MetersDTO> deleteMeters(
            @PathVariable("metersSerialNumber") String metersSerialNumber) {

        MetersEntity meters = metersRepository
                .findByMetersSerialNumber(metersSerialNumber)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Meter: \"%s\" does not exist.",
                                        metersSerialNumber
                                )
                        )
                );

        metersRepository.delete(meters);

        System.out.println("Deleted Meter: " + meters.getMetersSerialNumber());

        return ResponseEntity.noContent().build();
    }
}