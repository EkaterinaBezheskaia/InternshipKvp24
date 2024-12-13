package com.example.testtask.api.controllers;

import com.example.testtask.api.dto.HandbookAddressesDTO;
import com.example.testtask.api.exceptions.BadRequestException2;
import com.example.testtask.api.exceptions.NotFoundException2;
import com.example.testtask.api.factories.HandbookAddressesDTOFactory;
import com.example.testtask.store.entities.HandbookAddressesEntity;
import com.example.testtask.store.repositories.HandbookAddressesRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController

//TO DO: Сделать валидацию для обработки ожидаемых форматов данных с помощью библиотеки javax.validation

public class HandbookAddressesController {


    HandbookAddressesRepository handbookAddressesRepository;
    HandbookAddressesDTOFactory handbookAddressesDTOFactory;

    public static final String CREATE_ADDRESS = "/api/addresses";
    public static final String EDIT_ADDRESS = "/api/addresses/{street}/{number}/{literal}/{flat}";
    public static final String GET_ALL_ADDRESSES = "/api/addresses";
    public static final String GET_ADDRESS = "/{street}/{number}/{literal}/{flat}";
    public static final String GET_STREETS = "/api/streets/{street}";
    public static final String DELETE_ADDRESS = "/api/addresses/{street}/{number}/{literal}/{flat}";

    @PostMapping(CREATE_ADDRESS)
    public HandbookAddressesDTO createAddress(
            @RequestParam String street,
            @RequestParam Integer number,
            @RequestParam(required=false) String literal,
            @RequestParam(required=false) Integer flat) {

        handbookAddressesRepository
                .findByStreetAndNumberAndLiteralAndFlat(street, number, literal, flat)
                .ifPresent(existingAddresses -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Address \"%s\" \"%s\" \"%s\" \"%s\" already exists.",
                                    street, number, literal, flat
                            )
                    );
                });

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository.saveAndFlush(
                HandbookAddressesEntity.builder()
                        .street(street)
                        .number(number)
                        .literal(literal)
                        .build()
        );

        return handbookAddressesDTOFactory.makeHandbookAddressesDTO(handbookAddresses);
    }

    @PatchMapping(EDIT_ADDRESS)
    public HandbookAddressesDTO editAddress(
            @PathVariable("street") String street,
            @PathVariable("number") Integer number,
            @PathVariable(value = "literal", required = false) String literal,
            @PathVariable(value = "flat", required = false) Integer flat,
            @RequestParam String newStreet,
            @RequestParam Integer newNumber,
            @RequestParam(required=false) String newLiteral,
            @RequestParam(required=false) Integer newFlat) {

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository
                .findByStreetAndNumberAndLiteralAndFlat(street, number, literal, flat)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Address with \"%s\" \"%s\" \"%s\" \"%s\" does not exist.",
                                        street, number, literal, flat
                                )
                        )
                );

        handbookAddressesRepository
                .findByStreetAndNumberAndLiteralAndFlat(street, number, literal, flat)
                .ifPresent(existingAddresses -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Address \"%s\" \"%s\" \"%s\" \"%s\" already exists.",
                                    newStreet, newNumber, newLiteral, newFlat
                            )
                    );
                });

        handbookAddresses.setStreet(newStreet);
        handbookAddresses.setNumber(newNumber);
        handbookAddresses.setLiteral(newLiteral);
        handbookAddresses.setFlat(newFlat);

        handbookAddresses = handbookAddressesRepository.saveAndFlush(handbookAddresses);

        return handbookAddressesDTOFactory.makeHandbookAddressesDTO(handbookAddresses);
    }


    @GetMapping(HandbookAddressesController.GET_ADDRESS)
    public HandbookAddressesDTO getStreet(
            @PathVariable("street") String street,
            @PathVariable("number") Integer number,
            @PathVariable(value = "literal", required = false) String literal,
            @PathVariable(value = "flat", required = false) Integer flat) {

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository
                .findByStreetAndNumberAndLiteralAndFlat(street, number, literal, flat)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Address with \"%s\" \"%s\" \"%s\" \"%s\" does not exist.",
                                        street, number, literal, flat
                                )
                        )
                );

        System.out.println("Found Address: " + handbookAddresses.getStreet());

        return handbookAddressesDTOFactory.makeHandbookAddressesDTO(handbookAddresses);

    }

    @GetMapping(HandbookAddressesController.GET_STREETS)
    public List<HandbookAddressesDTO> getStreets(
            @PathVariable("street") String street,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "number") String[] sortBy) {

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository
                .findByStreet(street)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Address with \"%s\" does not exist.",
                                        street
                                )
                        )
                );

        Sort sort = Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(0, 10);
        List<HandbookAddressesEntity> addresses = handbookAddressesRepository
                .findByStreetOrderByNumberAsc(street, pageable);

        return addresses.stream()
                .map(handbookAddressesDTOFactory::makeHandbookAddressesDTO)
                .collect(Collectors.toList());

    }

    @GetMapping(GET_ALL_ADDRESSES)
    public List<HandbookAddressesDTO> getAllAddresses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "street") String[] sortBy) {

        Sort sort = Sort.by(sortBy).ascending();

        Page<HandbookAddressesEntity> allAddresses = handbookAddressesRepository
                .findAll(PageRequest.of(page, size, sort));

        return allAddresses.stream()
                .map(handbookAddressesDTOFactory::makeHandbookAddressesDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping(DELETE_ADDRESS)
    public ResponseEntity<HandbookAddressesDTO> deleteAddress(
            @PathVariable("street") String street,
            @PathVariable("number") Integer number,
            @PathVariable(value = "literal", required = false) String literal,
            @PathVariable(value = "flat", required = false) Integer flat) {

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository
                .findByStreetAndNumberAndLiteralAndFlat(street, number, literal, flat)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Address with \"%s\" \"%s\" \"%s\" \"%s\" does not exist.",
                                        street, number, literal, flat
                                )
                        )
                );

        handbookAddressesRepository.delete(handbookAddresses);

        System.out.println("Deleted Address: " + handbookAddresses.getStreet());

        return ResponseEntity.noContent().build();
    }

}



