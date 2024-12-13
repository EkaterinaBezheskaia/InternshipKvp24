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
    public static final String EDIT_ADDRESS = "/api/addresses/{titleAddress}/{number}/{literal}";
    public static final String GET_ALL_ADDRESSES = "/api/addresses";
    public static final String GET_ADDRESS = "/{titleAddress}/{number}/{literal}";
    public static final String GET_STREETS = "/api/streets/{titleAddress}";
    public static final String DELETE_ADDRESS = "/api/addresses/{titleAddress}/{number}/{literal}";

    @PostMapping(CREATE_ADDRESS)
    public HandbookAddressesDTO createAddress(
            @RequestParam String titleAddress,
            @RequestParam Integer number,
            @RequestParam String literal) {

        handbookAddressesRepository
                .findByTitleAddressAndNumberAndLiteral(titleAddress, number, literal)
                .ifPresent(handbookAddresses -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Address \"%s\" \"%s\" \"%s\" already exists.",
                                    titleAddress, number, literal
                            )
                    );
                });

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository.saveAndFlush(
                HandbookAddressesEntity.builder()
                        .titleAddress(titleAddress)
                        .number(number)
                        .literal(literal)
                        .build()
        );

        return handbookAddressesDTOFactory.makeHandbookAddressesDTO(handbookAddresses);
    }

    @PatchMapping(EDIT_ADDRESS)
    public HandbookAddressesDTO editAddress(
            @PathVariable("titleAddress") String titleAddress,
            @PathVariable("number") Integer number,
            @PathVariable("literal") String literal,
            @RequestParam String newTitleAddress,
            @RequestParam Integer newNumber,
            @RequestParam String newLiteral) {

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository
                .findByTitleAddressAndNumberAndLiteral(titleAddress, number, literal)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Address with \"%s\" \"%s\" \"%s\" does not exist.",
                                        titleAddress, number, literal
                                )
                        )
                );

        handbookAddressesRepository
                .findByTitleAddressAndNumberAndLiteral(newTitleAddress, newNumber, newLiteral)
                .ifPresent(existingAddresses -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Address \"%s\" \"%s\" \"%s\" already exists.",
                                    newTitleAddress, newNumber, newLiteral
                            )
                    );
                });

        handbookAddresses.setTitleAddress(newTitleAddress);
        handbookAddresses.setNumber(newNumber);
        handbookAddresses.setLiteral(newLiteral);

        handbookAddresses = handbookAddressesRepository.saveAndFlush(handbookAddresses);

        return handbookAddressesDTOFactory.makeHandbookAddressesDTO(handbookAddresses);
    }


    @GetMapping(HandbookAddressesController.GET_ADDRESS)
    public HandbookAddressesDTO getTitleAddress(
            @PathVariable("titleAddress") String titleAddress,
            @PathVariable("number") Integer number,
            @PathVariable("literal") String literal) {

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository
                .findByTitleAddressAndNumberAndLiteral(titleAddress, number, literal)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Address with \"%s\" \"%s\" \"%s\" does not exist.",
                                        titleAddress, number, literal
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
            @RequestParam(defaultValue = "number") String[] sortBy) {

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository
                .findByTitleAddress(titleAddress)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Address with \"%s\" does not exist.",
                                        titleAddress
                                )
                        )
                );

        Sort sort = Sort.by(sortBy).ascending();

        List<HandbookAddressesEntity> addresses = handbookAddressesRepository
                .findByTitleAddressOrderByNumberAsc(PageRequest.of(page, size, sort));

        return addresses.stream()
                .map(handbookAddressesDTOFactory::makeHandbookAddressesDTO)
                .collect(Collectors.toList());

    }

    @GetMapping(GET_ALL_ADDRESSES)
    public List<HandbookAddressesDTO> getAllAddresses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "titleAddress") String[] sortBy) {

        Sort sort = Sort.by(sortBy).ascending();

        Page<HandbookAddressesEntity> allAddresses = handbookAddressesRepository
                .findAll(PageRequest.of(page, size, sort));

        return allAddresses.stream()
                .map(handbookAddressesDTOFactory::makeHandbookAddressesDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping(DELETE_ADDRESS)
    public ResponseEntity<HandbookAddressesDTO> deleteAddress(
            @PathVariable("titleAddress") String titleAddress,
            @PathVariable("number") Integer number,
            @PathVariable("literal") String literal) {

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository
                .findByTitleAddressAndNumberAndLiteral(titleAddress, number, literal)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Address with \"%s\" \"%s\" \"%s\" does not exist.",
                                        titleAddress, number, literal
                                )
                        )
                );

        handbookAddressesRepository.delete(handbookAddresses);

        System.out.println("Deleted Address: " + handbookAddresses.getTitleAddress());

        return ResponseEntity.noContent().build();
    }

}



