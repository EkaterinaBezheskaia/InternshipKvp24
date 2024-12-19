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

import java.time.Instant;
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
    public static final String EDIT_ADDRESS = "/api/addresses/{street}/{number}";
    public static final String GET_ALL_ADDRESSES = "/api/addresses";
    public static final String GET_ADDRESS = "/{street}/{number}";
    public static final String GET_STREETS = "/api/streets/{street}";
    public static final String DELETE_ADDRESS = "/api/addresses/{street}/{number}";

    @PostMapping(CREATE_ADDRESS) //TO DO: при удалении записей id увеличивается, хз надо фиксить или нет
    // (если таблица пустая, то начать отсчет заново); Если вносить адрес полный, а потом без литерала/квартиры,
    // то кидает ошибку exists;
    // исправить время (часовой пояс нужен +4)
    public HandbookAddressesDTO createAddress(
            @RequestParam String street,
            @RequestParam Integer number,
            @RequestParam(required=false) String literal,
            @RequestParam(required=false) Integer flat) {

        literal = (literal == null) ? "" : literal;
        flat = (flat == null) ? 0 : flat;

        String finalLiteral = literal;
        Integer finalFlat = flat;

        handbookAddressesRepository
                .findByStreetAndNumberAndLiteralAndFlat(street, number, literal, flat)
                .ifPresent(existingAddresses -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Address \"%s\" \"%s\"%s%s already exists.",
                                    street, number,
                                    (!finalLiteral.isEmpty() ? String.format(" \"%s\"", finalLiteral) : ""),
                                    (finalFlat != 0 ? String.format(" \"%s\"", finalFlat) : "")
                            )
                    );
                });

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository.saveAndFlush(
                HandbookAddressesEntity.builder()
                        .street(street)
                        .number(number)
                        .literal(literal)
                        .flat(flat)
                        .createdAt(Instant.now())
                        .build()
        );

        return handbookAddressesDTOFactory.makeHandbookAddressesDTO(handbookAddresses);
    }

    @PatchMapping(EDIT_ADDRESS) //Если нужно поменять только улицу или только номер, то нужно исправить параметры на необязательные
    public HandbookAddressesDTO editAddress(
            @PathVariable("street") String street,
            @PathVariable("number") Integer number,
            @RequestParam(required=false) String literal,
            @RequestParam(required=false) Integer flat,
            @RequestParam(required=false) String newStreet,
            @RequestParam(required=false) Integer newNumber,
            @RequestParam(required=false) String newLiteral,
            @RequestParam(required=false) Integer newFlat) {

        literal = (literal == null) ? "" : literal;
        flat = (flat == null) ? 0 : flat;

        String finalLiteral = literal;
        Integer finalFlat = flat;

        HandbookAddressesEntity handbookAddresses = handbookAddressesRepository
                .findByStreetAndNumberAndLiteralAndFlat(street, number, literal, flat)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Address with \"%s\" \"%s\" \"%s\" \"%s\" does not exist.",
                                        street, number,
                                        (!finalLiteral.isEmpty() ? String.format(" \"%s\"", finalLiteral) : ""),
                                        (finalFlat != 0 ? String.format(" \"%s\"", finalFlat) : "")
                                )
                        )
                );

        newLiteral = (newLiteral == null) ? "" : newLiteral;
        newFlat = (newFlat == null) ? 0 : newFlat;

        String finalNewLiteral = newLiteral;
        Integer finalNewFlat = newFlat;
        handbookAddressesRepository
                .findByStreetAndNumberAndLiteralAndFlat(newStreet, newNumber, newLiteral, newFlat)
                .ifPresent(existingAddresses -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Address \"%s\" \"%s\" \"%s\" \"%s\" already exists.",
                                    (newStreet != null ? String.format(" \"%s\"", newStreet) : ""),
                                    (newNumber != null ? String.format(" \"%s\"", newNumber) : ""),
                                    (!finalNewLiteral.isEmpty() ? String.format(" \"%s\"", finalNewLiteral) : ""),
                                    (finalNewFlat != 0 ? String.format(" \"%s\"", finalNewFlat) : "")
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
    public List<HandbookAddressesDTO> getStreet(
            @PathVariable("street") String street,
            @PathVariable("number") Integer number,
            @RequestParam(required = false) String literal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "flat") String[] sortBy) {

        Sort sort = Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size);

        List<HandbookAddressesEntity> addresses = handbookAddressesRepository
                .findByStreetAndNumberOrderByLiteralAscFlatAsc(street, number, pageable); //Поиск по улице И по номеру

        if (addresses.isEmpty()) {
            throw new NotFoundException2(
                    String.format("Не найдено адресов для улицы \"%s\".", street)
            );
        }

        return addresses.stream()
                .map(handbookAddressesDTOFactory::makeHandbookAddressesDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(HandbookAddressesController.GET_STREETS) //TO DO: выдает только один результат, на нескольких кидает ошибку
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
                    String.format("Не найдено адресов для улицы \"%s\".", street)
            );
        }

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

    @DeleteMapping(DELETE_ADDRESS) //TO DO: Не ищет адреса
    public ResponseEntity<HandbookAddressesDTO> deleteAddress(
            @PathVariable("street") String street,
            @PathVariable("number") Integer number,
            @RequestParam(required = false) String literal,
            @RequestParam(required = false) Integer flat) {

        List<HandbookAddressesEntity> addresses = handbookAddressesRepository
                .findByStreetAndNumber(street, number);

        if (literal == null && flat == null) {
            // Удаляем только адреса с null literal и flat
            addresses = addresses.stream()
                    .filter(address -> address.getLiteral() == null && address.getFlat() == null)
                    .collect(Collectors.toList());
        } else if (literal != null && flat == null) {
            // Удаляем только адреса с указанным literal и null flat
            addresses = addresses.stream()
                    .filter(address -> literal.equals(address.getLiteral()) && address.getFlat() == null)
                    .collect(Collectors.toList());
        } else if (literal == null && flat != null) {
            // Удаляем только адреса с null literal и указанным flat
            addresses = addresses.stream()
                    .filter(address -> address.getLiteral() == null && flat.equals(address.getFlat()))
                    .collect(Collectors.toList());
        } else if (literal != null && flat != null) {
            // Удаляем только адреса с указанными literal и flat
            addresses = addresses.stream()
                    .filter(address -> literal.equals(address.getLiteral()) && flat.equals(address.getFlat()))
                    .collect(Collectors.toList());
        }

        // Если после фильтрации список пуст, выбрасываем исключение
        if (addresses.isEmpty()) {
            throw new NotFoundException2(
                    String.format(
                            "Address with \"%s\" \"%s\"%s%s does not exist.",
                            street, number,
                            (literal != null && !literal.isEmpty() ? String.format(" \"%s\"", literal) : ""),
                            (flat != null ? String.format(" \"%s\"", flat) : "")
                    )
            );
        }

        for (HandbookAddressesEntity address : addresses) {
            handbookAddressesRepository.delete(address);
            System.out.println("Deleted Address: " + address.getStreet() + " " + address.getNumber() + " " + address.getLiteral() + " " + address.getFlat());
        }

        return ResponseEntity.noContent().build();
    }

}



