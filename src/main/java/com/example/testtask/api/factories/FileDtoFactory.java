package com.example.testtask.api.factories;

import com.example.testtask.api.dto.FileDto;
import com.example.testtask.store.entities.FileEntity;
import org.springframework.stereotype.Component;

@Component
public class FileDtoFactory {

    public FileDto makeFileDto(FileEntity entity) {

        return FileDto.fileDtoBuilder()
                .id(entity.getId())
                .title(entity.getTitle())
                .base64File(entity.getBase64File())
                .creationDate(entity.getCreationDate())
                .description(entity.getDescription())
                .build();
    }

}
