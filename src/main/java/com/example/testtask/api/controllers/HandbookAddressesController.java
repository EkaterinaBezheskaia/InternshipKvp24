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
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.boot.autoconfigure.container.ContainerImageMetadata.isPresent;
import static org.springframework.data.repository.util.ClassUtils.ifPresent;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
@Component
@Repository


public class HandbookAddressesController {

    HandbookAddressesRepository handbookAddressesRepository;

    HandbookAddressesDTOFactory handbookAddressesDTOFactory;

    public static final String CREATE_ADDRESS = "/api/addresses";
    public static final String EDIT_ADDRESS = "/api/addresses/{titleAddress}";
    public static final String UPDATE_ADDRESS = "/api/addresses/{id}";
    public static final String GET_ALL_ADDRESSES = "/api/addresses";
    public static final String GET_ADDRESS = "/{titleAddress}";
    public static final String DELETE_ADDRESS = "/api/addresses/{titleAddress}";

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

}


/*    @GetMapping(GET_ADDRESS)
    public HandbookAddressesDTO getTitleAddress(
            @PathVariable String titleAddress) throws BadRequestException {

        return handbookAddressesRepository.findByTitleAddress(titleAddress)
                .orElseThrow(() -> new BadRequestException("File not found"));
    }

    @GetMapping(GET_ALL_ADDRESSES)
    public List<HandbookAddressesDTO> getAllFiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy) {

        Page<HandbookAddressesEntity> filesPage = handbookAddressesRepository.findAll(PageRequest.of(page, size, Sort.by(sortBy).descending()));

        return filesPage.stream()
                .map(handbookAddressesDTOFactory::makeHandbookAddressesDTO)
                .collect(Collectors.toList());
    }

 */



