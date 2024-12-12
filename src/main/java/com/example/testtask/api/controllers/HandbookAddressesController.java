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
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
//@Component
//@Repository

//TO DO: Сделать валидацию для обработки ожидаемых форматов данных с помощью библиотеки javax.validation

public class HandbookAddressesController {


    HandbookAddressesRepository handbookAddressesRepository;
    HandbookAddressesDTOFactory handbookAddressesDTOFactory;

    public static final String CREATE_ADDRESS = "/api/addresses";
    public static final String EDIT_ADDRESS = "/api/addresses/{titleAddress}/{literal}";
    public static final String GET_ALL_ADDRESSES = "/api/addresses";
    public static final String GET_ADDRESS = "/{titleAddress}/{literal}";
    public static final String GET_STREETS = "/api/streets";
    public static final String DELETE_ADDRESS = "/api/addresses/{titleAddress}/{literal}";

    @PostMapping(CREATE_ADDRESS)
    public HandbookAddressesDTO createAddress(
            @RequestParam String titleAddress,
            @RequestParam String literal) {

        handbookAddressesRepository
                .findByTitleAddressAndLiteral(titleAddress, literal)
                .ifPresent(handbookAddresses -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Address \"%s\" with literal \"%s\" already exists.",
                                    titleAddress, literal
                            )
                    );
                });

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository.saveAndFlush(
                HandbookAddressesEntity.builder()
                        .titleAddress(titleAddress)
                        .literal(literal)
                        .build()
        );

        return handbookAddressesDTOFactory.makeHandbookAddressesDTO(handbookAddresses);
    }

    @PatchMapping(EDIT_ADDRESS)
    public HandbookAddressesDTO editAddress(
            @PathVariable("titleAddress") String titleAddress,
            @PathVariable("literal") String literal,
            @RequestParam String newTitleAddress,
            @RequestParam String newLiteral) {

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository
                .findByTitleAddressAndLiteral(titleAddress, literal)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Address with \"%s\" with literal \"%s\" does not exist.",
                                        titleAddress, literal
                                )
                        )
                );

        handbookAddressesRepository
                .findByTitleAddressAndLiteral(newTitleAddress, newLiteral)
                .ifPresent(existingAddresses -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Address \"%s\" with literal \"%s\" already exists.",
                                    newTitleAddress, newLiteral
                            )
                    );
                });

        handbookAddresses.setTitleAddress(newTitleAddress);
        handbookAddresses.setLiteral(newLiteral);

        handbookAddresses = handbookAddressesRepository.saveAndFlush(handbookAddresses);

        return handbookAddressesDTOFactory.makeHandbookAddressesDTO(handbookAddresses);
    }


    @GetMapping(HandbookAddressesController.GET_ADDRESS)
    public HandbookAddressesDTO getTitleAddressAndLiteral(
            @PathVariable("titleAddress") String titleAddress,
            @PathVariable("literal") String literal) {

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository
                .findByTitleAddressAndLiteral(titleAddress, literal)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Address with \"%s\" with literal \"%s\" does not exist.",
                                        titleAddress, literal
                                )
                        )
                );

        System.out.println("Found Address: " + handbookAddresses.getTitleAddress());

        return handbookAddressesDTOFactory.makeHandbookAddressesDTO(handbookAddresses);

    }

    @GetMapping(HandbookAddressesController.GET_STREETS)
    public List<HandbookAddressesDTO> getStreets(
            @PathVariable("titleAddress") String titleAddress,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "literal") String sortBy) {

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository
                .findByTitleAddress(titleAddress)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Address with \"%s\" with literal \"%s\" does not exist.",
                                        titleAddress
                                )
                        )
                );

        List<HandbookAddressesEntity> addresses = handbookAddressesRepository
                .findByTitleAddressOrderByLiteralAsc(PageRequest.of(page, size, Sort.by(sortBy).descending()));

        return addresses.stream()
                .map(handbookAddressesDTOFactory::makeHandbookAddressesDTO)
                .collect(Collectors.toList());

    }

    @GetMapping(GET_ALL_ADDRESSES)
    public List<HandbookAddressesDTO> getAllAddresses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "titleAddress") String sortBy) {

        Page<HandbookAddressesEntity> allAddresses = handbookAddressesRepository
                .findAll(PageRequest.of(page, size, Sort.by(sortBy).descending()));

        return allAddresses.stream()
                .map(handbookAddressesDTOFactory::makeHandbookAddressesDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping(DELETE_ADDRESS)
    public ResponseEntity<HandbookAddressesDTO> deleteAddress(
            @PathVariable("titleAddress") String titleAddress,
            @PathVariable("literal") String literal) {

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository
                .findByTitleAddressAndLiteral(titleAddress, literal)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Address with \"%s\" with literal \"%s\" does not exist.",
                                        titleAddress, literal
                                )
                        )
                );

        handbookAddressesRepository.delete(handbookAddresses);

        System.out.println("Deleted Address: " + handbookAddresses.getTitleAddress());

        return ResponseEntity.noContent().build();
    }

}



