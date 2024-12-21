package com.example.testtask.api.factories;

import com.example.testtask.api.dto.MeterReadingsDTO;
import com.example.testtask.store.entities.MeterReadingsEntity;
import org.springframework.stereotype.Component;

@Component
public class MeterReadingsDTOFactory {

    public MeterReadingsDTO makeMeterReadingsDTO(MeterReadingsEntity entity) {

        return MeterReadingsDTO.fileDtoBuilder()
                .id(entity.getId())
                .readingsDate(entity.getReadingsDate())
                .readings(entity.getReadings())
                .createdAt(entity.getCreatedAt())
                .createdAtLocal(entity.getCreatedAtLocal())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}