package com.example.testtask.api.controllers;

import com.example.testtask.api.dto.HandbookAddressesDTO;
import com.example.testtask.api.exceptions.BadRequestException2;
import com.example.testtask.api.exceptions.NotFoundException2;
import com.example.testtask.api.factories.HandbookAddressesDTOFactory;
import com.example.testtask.store.entities.HandbookAddressesEntity;
import com.example.testtask.store.repositories.HandbookAddressesRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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

/**
 * Контроллер для работы с адресами.
 */
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

    /**
     * Создает новый адрес.
     *
     * @param street название улицы
     * @param number номер дома
     * @param literal литерал (необязательный)
     * @param flat номер квартиры (необязательный)
     * @return созданный адрес в виде DTO
     * @throws BadRequestException2 если адрес уже существует
     */
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

    /**
     * Обновляет существующий адрес.
     *
     * @param street старая улица
     * @param number старый номер дома
     * @param literal старая литерал (необязательный)
     * @param flat старый номер квартиры (необязательный)
     * @param newStreet новая улица (необязательный)
     * @param newNumber новый номер дома (необязательный)
     * @param newLiteral новый литерал (необязательный)
     * @param newFlat новый номер квартиры (необязательный)
     * @return обновленный адрес в виде DTO
     * @throws NotFoundException2 если адрес не найден
     * @throws BadRequestException2 если новый адрес уже существует
     */
    @PatchMapping(EDIT_ADDRESS)
    public HandbookAddressesDTO editAddress(
            @Valid
            @PathVariable(name = "street", required = false) String street,
            @PathVariable(name = "number", required = false) int number,
            @RequestParam(required=false) String literal,
            @RequestParam(required=false) int flat,
            @RequestParam(required=false) String newStreet,
            @RequestParam(required=false) int newNumber,
            @RequestParam(required=false) String newLiteral,
            @RequestParam(required=false) int newFlat) {

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

    /**
     * Получает все адреса с пагинацией и сортировкой.
     *
     * @param page номер страницы
     * @param size размер страницы
     * @param sortBy поля для сортировки
     * @return список адресов в виде DTO
     * @throws NotFoundException2 если адреса не найдены
     */
    @GetMapping(GET_ALL_ADDRESSES)
    public List<HandbookAddressesDTO> getAllAddresses(
            @Valid
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

    /**
     * Получает адреса по улице с пагинацией и сортировкой.
     *
     * @param street название улицы
     * @param page номер страницы
     * @param size размер страницы
     * @param sortBy поля для сортировки
     * @return список адресов в виде DTO
     * @throws NotFoundException2 если адреса не найдены
     */
    @GetMapping(GET_STREETS)
    public List<HandbookAddressesDTO> getStreets(
            @Valid
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
                            street )
            );
        }

        return addresses.stream()
                .map(handbookAddressesDTOFactory::makeHandbookAddressesDTO)
                .collect(Collectors.toList());
    }

    /**
     * Получает адрес по улице и номеру с пагинацией и сортировкой.
     *
     * @param street название улицы
     * @param number номер дома
     * @param literal литерал (необязательный)
     * @param page номер страницы
     * @param size размер страницы
     * @param sortBy поля для сортировки
     * @return список адресов в виде DTO
     * @throws NotFoundException2 если адреса не найдены
     */
    @GetMapping(GET_ADDRESS)
    public List<HandbookAddressesDTO> getStreet(
            @Valid
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

    /**
     * Удаляет адрес по улице и номеру.
     *
     * @param street название улицы
     * @param number номер дома
     * @param literal литерал (необязательный)
     * @param flat номер квартиры (необязательный)
     * @return ответ без содержимого
     * @throws NotFoundException2 если адрес не найден
     */
    @DeleteMapping(DELETE_ADDRESS)
    public ResponseEntity<HandbookAddressesDTO> deleteAddress(
            @Valid
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

    /**
     * Удаляет все адреса.
     *
     * @return ответ без содержимого
     */
    @DeleteMapping(DELETE_ALL_ADDRESS)
    public ResponseEntity<HandbookAddressesDTO> deleteAllAddresses() {

        handbookAddressesRepository.deleteAll();

        System.out.println("Удалены все адреса!");

        return ResponseEntity.noContent().build();
    }
}



