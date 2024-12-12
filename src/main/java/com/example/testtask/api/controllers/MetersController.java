package com.example.testtask.api.controllers;

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
public class MetersController {

    FileRepository fileRepository;

    FileDtoFactory fileDtoFactory;

    public static final String GET_ALL_FILES = "/api/files";
    public static final String GET_FILE = "/{id}";
    public static final String CREATE_FILE = "/api/files/{id}";


    @PostMapping(CREATE_FILE)
    public Long createFile(
            @RequestBody FileDto fileDto) throws BadRequestException {
        if (fileDto.getBase64File().isEmpty()) {
            throw new BadRequestException("File content can't be empty.");
        }

        FileEntity fileEntity = FileEntity.builder()
                .title(fileDto.getTitle())
                .creationDate(fileDto.getCreationDate())
                .description(fileDto.getDescription())
                .base64File(fileDto.getBase64File())
                .build();

        FileEntity savedFile = fileRepository.saveAndFlush(fileEntity);
        return savedFile.getId();
    }

    @GetMapping(GET_FILE)
    public FileDto getFile(
            @PathVariable Long id) throws BadRequestException {

        return fileRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("File not found"));
    }

    @GetMapping(GET_ALL_FILES)
    public List<FileDto> getAllFiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy) {

        Page<FileEntity> filesPage = fileRepository.findAll(PageRequest.of(page, size, Sort.by(sortBy).descending()));

        return filesPage.stream()
                .map(fileDtoFactory::makeFileDto)
                .collect(Collectors.toList());
    }
}