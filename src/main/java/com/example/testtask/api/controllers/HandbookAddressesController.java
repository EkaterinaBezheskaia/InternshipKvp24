package com.example.testtask.api.controllers;

import com.example.testtask.api.dto.HandbookAddressesDTO;
import com.example.testtask.api.exceptions.BadRequestException2;
import com.example.testtask.api.exceptions.NotFoundException2;
import com.example.testtask.api.factories.HandbookAddressesDTOFactory;
import com.example.testtask.store.entities.HandbookAddressesEntity;
import com.example.testtask.store.repositories.HandbookAddressesRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

public class HandbookAddressesController {

    HandbookAddressesRepository handbookAddressesRepository;
    HandbookAddressesDTOFactory handbookAddressesDTOFactory;

    public static final String CREATE_ADDRESS = "/api/addresses";
    public static final String EDIT_ADDRESS = "/api/addresses/{street}/{number}";
    public static final String GET_ALL_ADDRESSES = "/api/addresses";
    public static final String GET_STREETS = "/api/addresses/{street}";
    public static final String GET_ADDRESS = "/api/addresses/{street}/{number}";
    public static final String DELETE_ADDRESS = "/api/addresses/{street}/{number}";
    public static final String DELETE_ALL_ADDRESS = "/api/reset";

    @PostMapping(CREATE_ADDRESS)
    public HandbookAddressesDTO createAddress(
            @Valid
            @RequestParam String street,
            @RequestParam int number,
            @RequestParam(required=false) String literal,
            @RequestParam(required=false) int flat) {

        literal = (literal == null) ? "" : literal;
        flat = (flat == 0) ? 0 : flat;

        String finalLiteral = literal;
        int finalFlat = flat;

        if (handbookAddressesRepository.existsByStreetAndNumberAndLiteralAndFlat(street, number, literal, flat)) {
            throw new BadRequestException2(
                    String.format(
                            "Адрес \"%s\" \"%s\"%s%s уже существует.",
                            street, number,
                            (!finalLiteral.isEmpty() ? String.format(" \"%s\"", finalLiteral) : ""),
                            (finalFlat != 0 ? String.format(" \"%s\"", finalFlat) : "")
                    )
            );
        }

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository.saveAndFlush(
                HandbookAddressesEntity.builder()
                        .street(street)
                        .number(number)
                        .literal(literal)
                        .flat(flat)
                        .createdAt(Instant.now())
                        .createdAtLocal(LocalDateTime.now(ZoneId.systemDefault()))
                        .build()
        );

        return handbookAddressesDTOFactory.makeHandbookAddressesDTO(handbookAddresses);
    }

    @PatchMapping(EDIT_ADDRESS)
    public HandbookAddressesDTO editAddress(
            @PathVariable(name = "street", required = false) String street,
            @PathVariable(name = "number", required = false) int number,
            @RequestParam(required=false) String literal,
            @RequestParam(required=false) int flat,
            @RequestParam(required=false) @Size(max = 100, message = "Название новой улицы не должно превышать 100 символов") String newStreet,
            @RequestParam(required=false) @Min(value = 0, message = "Новый номер дома должен быть положительным") int newNumber,
            @RequestParam(required=false) @Size(max = 1, message = "Новый литерал не должен превышать 1 символа") String newLiteral,
            @RequestParam(required=false) @Min(value = 0, message = "Новый номер квартиры должен быть положительным") int newFlat) {

        String finalStreet = street != null ? street : "";
        int finalNumber = number != 0 ? number : 0;
        String finalLiteral = literal != null ? literal : "";
        int finalFlat = flat != 0 ? flat : 0;

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository
                .findByStreetAndNumberAndLiteralAndFlat(street, number, literal, flat)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Адрес \"%s\" \"%s\" \"%s\" \"%s\" не существует.",
                                        (!finalStreet.isEmpty() ? String.format(" \"%s\"", finalStreet) : ""),
                                        (finalNumber != 0 ? String.format(" \"%s\"", finalNumber) : ""),
                                        (!finalLiteral.isEmpty() ? String.format(" \"%s\"", finalLiteral) : ""),
                                        (finalFlat != 0 ? String.format(" \"%s\"", finalFlat) : "")
                                )
                        )
                );

        String finalNewStreet = newStreet != null ? newStreet : "";
        int finalNewNumber = newNumber != 0 ? newNumber : 0;
        String finalNewLiteral = newLiteral != null ? newLiteral : "";
        int finalNewFlat = newFlat != 0 ? newFlat : 0;
        if (handbookAddressesRepository.existsByStreetAndNumberAndLiteralAndFlat(newStreet, newNumber, newLiteral, newFlat)) {
            throw new BadRequestException2(
                            String.format(
                                    "Адрес \"%s\" \"%s\" \"%s\" \"%s\" уже существует.",
                                    (finalNewStreet.isEmpty() ? String.format(" \"%s\"", finalNewStreet) : ""),
                                    (finalNewNumber != 0 ? String.format(" \"%s\"", finalNewNumber) : ""),
                                    (!finalNewLiteral.isEmpty() ? String.format(" \"%s\"", finalNewLiteral) : ""),
                                    (finalNewFlat != 0 ? String.format(" \"%s\"", finalNewFlat) : "")
                            )
                    );
        }

        handbookAddresses.setUpdatedAt(Instant.now());

        handbookAddresses.setStreet(newStreet);
        handbookAddresses.setNumber(newNumber);
        handbookAddresses.setLiteral(newLiteral);
        handbookAddresses.setFlat(newFlat);

        handbookAddresses = handbookAddressesRepository.saveAndFlush(handbookAddresses);

        return handbookAddressesDTOFactory.makeHandbookAddressesDTO(handbookAddresses);
    }

    @GetMapping(GET_ALL_ADDRESSES)
    public List<HandbookAddressesDTO> getAllAddresses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "street") String[] sortBy) {

        Sort sort = Sort.by(sortBy).ascending();

        Page<HandbookAddressesEntity> allAddresses = handbookAddressesRepository
                .findAll(PageRequest.of(page, size, sort));

        if (allAddresses.isEmpty()) {
            throw new NotFoundException2(
                    "Не найдено адресов"
            );
        }

        return allAddresses.stream()
                .map(handbookAddressesDTOFactory::makeHandbookAddressesDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(HandbookAddressesController.GET_STREETS)
    public List<HandbookAddressesDTO> getStreets(
            @PathVariable("street") String street,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "number") String[] sortBy) {

        Sort sort = Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size);

        List<HandbookAddressesEntity> addresses = handbookAddressesRepository
                .findByStreetOrderByNumberAsc(street, pageable);

        if (addresses.isEmpty()) {
            throw new NotFoundException2(
                    String.format(
                            "Не найдено адресов для улицы \"%s\".",
                            street
                    )
            );
        }

        return addresses.stream()
                .map(handbookAddressesDTOFactory::makeHandbookAddressesDTO)
                .collect(Collectors.toList());

    }

    @GetMapping(HandbookAddressesController.GET_ADDRESS)
    public List<HandbookAddressesDTO> getStreet(
            @PathVariable("street") String street,
            @PathVariable("number") int number,
            @RequestParam(required = false) String literal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "flat") String[] sortBy) {

        Sort sort = Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size);

        List<HandbookAddressesEntity> addresses = handbookAddressesRepository
                .findByStreetAndNumberOrderByLiteralAscFlatAsc(street, number, pageable);

        if (addresses.isEmpty()) {
            throw new NotFoundException2(
                    String.format(
                            "Не найдено адресов для улицы \"%s\".",
                            street
                    )
            );
        }

        return addresses.stream()
                .map(handbookAddressesDTOFactory::makeHandbookAddressesDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping(DELETE_ADDRESS)
    public ResponseEntity<HandbookAddressesDTO> deleteAddress(
            @PathVariable("street") String street,
            @PathVariable("number") int number,
            @RequestParam(required = false) String literal,
            @RequestParam(required = false) Integer flat) {

        literal = (literal == null) ? "" : literal;
        flat = (flat == 0) ? 0 : flat;

        String finalLiteral = literal;
        int finalFlat = flat;

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository
                .findByStreetAndNumberAndLiteralAndFlat(street, number, literal, flat)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Адрес \"%s\" \"%s\" \"%s\" \"%s\" не существует.",
                                        street, number,
                                        (!finalLiteral.isEmpty() ? String.format(" \"%s\"", finalLiteral) : ""),
                                        (finalFlat != 0 ? String.format(" \"%s\"", finalFlat) : "")
                                )
                        )
                );


        handbookAddressesRepository.delete(handbookAddresses);
        System.out.println("Удален адрес: " + handbookAddresses.getStreet() + " " + handbookAddresses.getNumber() +
                " " + handbookAddresses.getLiteral() + " " + handbookAddresses.getFlat());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(DELETE_ALL_ADDRESS)
    public ResponseEntity<HandbookAddressesDTO> deleteAllAddresses() {

        handbookAddressesRepository.deleteAll();

        System.out.println("Удалены все адреса!");

        return ResponseEntity.noContent().build();
    }

}



