package com.example.testtask.api.factories;

import com.example.testtask.api.dto.HandbookMeterTypesDTO;
import com.example.testtask.store.entities.HandbookMeterTypesEntity;
import org.springframework.stereotype.Component;

@Component
public class HandbookMeterTypesDTOFactory {

    public HandbookMeterTypesDTO makeHandbookMeterTypesDTO(HandbookMeterTypesEntity entity) {

        return HandbookMeterTypesDTO.fileDtoBuilder()
                .id(entity.getId())
                .meterTypeTitle(entity.getMeterTypeTitle())
                .createdAt(entity.getCreatedAt())
                .createdAtLocal(entity.getCreatedAtLocal())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

}
