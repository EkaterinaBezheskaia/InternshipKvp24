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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с приборами учета.
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
public class MetersController {

    MetersRepository metersRepository;
    MetersDTOFactory metersDTOFactory;
    HandbookAddressesRepository handbookAddressesRepository;

    public static final String CREATE_METERS = "/api/meters";
    public static final String EDIT_METERS = "/api/meters/{metersSerialNumber}";
    public static final String EDIT_DATE = "/api/meters/{metersSerialNumber}/{installationDate}";
    public static final String GET_ALL_METERS = "/api/meters";
    public static final String GET_METER = "/api/meters/{metersSerialNumber}";
    public static final String DELETE_METER = "/api/meters/{metersSerialNumber}";
    public static final String DELETE_ALL_METERS = "/api/meters";

    /**
     * Создает новый прибор учета.
     *
     * @param metersSerialNumber серийный номер прибора учета
     * @param street название улицы
     * @param number номер дома
     * @param literal литерал (необязательный)
     * @param flat номер квартиры (необязательный)
     * @param installationDate дата установки
     * @return созданный прибор учета в виде DTO
     * @throws BadRequestException2 если прибор учета уже существует
     * @throws NotFoundException2 если адрес не найден
     */
    @PostMapping(CREATE_METERS)
    public MetersDTO createMetersSerialNumber(
            @Valid
            @RequestParam String metersSerialNumber,
            @RequestParam String street,
            @RequestParam int number,
            @RequestParam(required=false) String literal,
            @RequestParam(required=false) int flat,
            @RequestParam LocalDate installationDate) {

        metersRepository
                .findByMetersSerialNumber(metersSerialNumber)
                .ifPresent(meters -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Прибор учета с серийным номером \"%s\" уже существует.",
                                    metersSerialNumber
                            )
                    );
                });

        literal = (literal == null) ? "" : literal;
        flat = (flat == 0) ? 0 : flat;

        String finalLiteral = literal;
        Integer finalFlat = flat;

        HandbookAddressesEntity address = handbookAddressesRepository
                .findByStreetAndNumberAndLiteralAndFlat(street, number, finalLiteral, finalFlat)
                .orElseThrow(() -> new NotFoundException2(
                        String.format(
                                "Адрес \"%s\" \"%s\"%s%s не существует.",
                                street, number,
                                (!finalLiteral.isEmpty() ? String.format(" \"%s\"", finalLiteral) : ""),
                                (finalFlat != 0 ? String.format(" \"%s\"", finalFlat) : "")
                        )
                ));

        MetersEntity meters = metersRepository.saveAndFlush(
                MetersEntity.builder()
                        .metersSerialNumber(metersSerialNumber)
                        .address(address)
                        .installationDate(installationDate)
                        .createdAt(Instant.now())
                        .createdAtLocal(LocalDateTime.now(ZoneId.systemDefault()))
                        .build()
        );

        return metersDTOFactory.makeMetersDTO(meters);
    }

    /**
     * Обновляет существующий прибор учета.
     *
     * @param metersSerialNumber старый серийный номер
     * @param newMetersSerialNumber новый серийный номер
     * @return обновленный прибор учета в виде DTO
     * @throws NotFoundException2 если прибор учета не найден
     * @throws BadRequestException2 если новый прибор учета уже существует
     */
    @PatchMapping(EDIT_METERS)
    public MetersDTO editMeter(
            @Valid
            @PathVariable("metersSerialNumber") String metersSerialNumber,
            @RequestParam String newMetersSerialNumber) {

        MetersEntity meters = metersRepository
                .findByMetersSerialNumber(metersSerialNumber)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Прибор учета с серийным номером \"%s\" не существует.",
                                        metersSerialNumber
                                )
                        )
                );

        metersRepository
                .findByMetersSerialNumber(newMetersSerialNumber)
                .ifPresent(existingMeters -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Прибор учета с серийным номером \"%s\" уже существует.",
                                    newMetersSerialNumber
                            )
                    );
                });

        meters.setUpdatedAt(Instant.now());
        meters.setMetersSerialNumber(newMetersSerialNumber);

        meters = metersRepository.saveAndFlush(meters);

        return metersDTOFactory.makeMetersDTO(meters);
    }

    /**
     * Обновляет дату установки прибора учета.
     *
     * @param metersSerialNumber серийный номер прибора учета
     * @param installationDate старая дата установки
     * @param newInstallationDate новая дата установки
     * @return обновленный прибор учета в виде DTO
     * @throws NotFoundException2 если прибор учета не найден
     * @throws BadRequestException2 если новый прибор учета с данной датой уже существует
     */
    @PatchMapping(EDIT_DATE)
    public MetersDTO editDate(
            @Valid
            @PathVariable("metersSerialNumber") String metersSerialNumber,
            @PathVariable("installationDate") LocalDate installationDate,
            @RequestParam LocalDate newInstallationDate) {

        MetersEntity meters = metersRepository
                .findByMetersSerialNumber(metersSerialNumber)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Прибор учета с серийным номером \"%s\" не существует.",
                                        metersSerialNumber
                                )
                        )
                );

        metersRepository
                .findByMetersSerialNumberAndInstallationDate(metersSerialNumber, installationDate)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Прибор учета с серийным номером \"%s\" и датой установки \"%s\" не существует.",
                                        metersSerialNumber, installationDate
                                )
                        )
                );

        metersRepository
                .findByMetersSerialNumberAndInstallationDate(metersSerialNumber, newInstallationDate)
                .ifPresent(existingMeters -> {
                    throw new BadRequestException2(
                            String.format(
                                    "Прибор учета с серийным номером \"%s\" и датой установки \"%s\" уже существует.",
                                    metersSerialNumber, newInstallationDate
                            )
                    );
                });

        meters.setUpdatedAt(Instant.now());
        meters.setInstallationDate(newInstallationDate);

        meters = metersRepository.saveAndFlush(meters);

        return metersDTOFactory.makeMetersDTO(meters);
    }

    /**
     * Получает все приборы учета с пагинацией и сортировкой.

     * @param page номер страницы
     * @param size размер страницы
     * @param sortBy поля для сортировки
     * @return список приборов учета в виде DTO
     * @throws NotFoundException2 если приборы учета не найдены
     */
    @GetMapping(GET_ALL_METERS)
    public List<MetersDTO> getAllMeters(
            @Valid
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "metersSerialNumber") String[] sortBy) {

        Sort sort = Sort.by(sortBy).ascending();

        Page<MetersEntity> allMeters = metersRepository
                .findAll(PageRequest.of(page, size, sort));

        if (allMeters.isEmpty()) {
            throw new NotFoundException2(
                    "Не найдено приборов учета"
            );
        }

        return allMeters.stream()
                .map(metersDTOFactory::makeMetersDTO)
                .collect(Collectors.toList());
    }

    /**
     * Получает прибор учета по серийному номеру.
     *
     * @param metersSerialNumber серийный номер прибора учета
     * @return прибор учета в виде DTO
     * @throws NotFoundException2 если прибор учета не найден
     */
    @GetMapping(GET_METER)
    public MetersDTO getTitleMeters(
            @Valid
            @PathVariable("metersSerialNumber") String metersSerialNumber) {

        MetersEntity meters = metersRepository
                .findByMetersSerialNumber(metersSerialNumber)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Прибор учета с серийным номером \"%s\" не существует.",
                                        metersSerialNumber
                                )
                        )
                );

        System.out.println("Found Meter: " + meters.getMetersSerialNumber());

        return metersDTOFactory.makeMetersDTO(meters);
    }

    /**
     * Удаляет прибор учета по серийному номеру.
     *
     * @param metersSerialNumber серийный номер прибора учета
     * @return ответ без содержимого
     * @throws NotFoundException2 если прибор учета не найден
     */
    @DeleteMapping(DELETE_METER)
    public ResponseEntity<MetersDTO> deleteMeters(
            @Valid
            @PathVariable("metersSerialNumber") String metersSerialNumber) {

        MetersEntity meters = metersRepository
                .findByMetersSerialNumber(metersSerialNumber)
                .orElseThrow(() ->
                        new NotFoundException2(
                                String.format(
                                        "Прибор учета с серийным номером \"%s\" не существует.",
                                        metersSerialNumber
                                )
                        )
                );

        metersRepository.delete(meters);

        System.out.println("Удален прибор учета с серийным номером: " + meters.getMetersSerialNumber());

        return ResponseEntity.noContent().build();
    }

    /**
     * Удаляет все приборы учета.
     *
     * @return ответ без содержимого
     */
    @DeleteMapping(DELETE_ALL_METERS)
    public ResponseEntity<MetersDTO> deleteAllMeters() {

        metersRepository.deleteAll();

        System.out.println("Удалены все приборы учета!");

        return ResponseEntity.noContent().build();
    }
}