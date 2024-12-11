package com.example.testtask.api.controllers;

import com.example.testtask.api.dto.HandbookAddressesDTO;
import com.example.testtask.api.factories.HandbookAddressesDTOFactory;
import com.example.testtask.store.entities.FileEntity;
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

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
@Component
@Repository

public class HandbookAddressesController {

    HandbookAddressesRepository handbookAddressesRepository;

    HandbookAddressesDTOFactory handbookAddressesDTOFactory;

    public static final String GET_ALL_ADDRESSES = "/api/addresses";
    public static final String GET_ADDRESS = "/{titleAddress}";
    public static final String CREATE_ADDRESS = "/api/addresses/{titleAddress}";
    public static final String DELETE_ADDRESS = "/api/addresses/{titleAddress}";


    @PostMapping(CREATE_ADDRESS)
    public HandbookAddressesDTO createAddress(
            @RequestBody HandbookAddressesDTO handbookAddressesDTO) throws BadRequestException {
        if (handbookAddressesDTO.getBase64File().isEmpty()) {
            throw new BadRequestException("File content can't be empty.");
        }

        /*HandbookAddressesEntity handbookAddressesEntity = HandbookAddressesEntity.builder()
                .title(handbookAddressesDTO.getTitle())
                .creationDate(handbookAddressesDTO.getCreationDate())
                .description(handbookAddressesDTO.getDescription())
                .base64File(handbookAddressesDTO.getBase64File())
                .build();
        */

        HandbookAddressesEntity savedFile = handbookAddressesRepository.saveAndFlush(handbookAddressesEntity);
        return savedFile.getId();
    }

    @GetMapping(GET_ADDRESS)
    public HandbookAddressesDTO getFile(
            @PathVariable Long id) throws BadRequestException {

        return handbookAddressesRepository.findById(id)
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
}