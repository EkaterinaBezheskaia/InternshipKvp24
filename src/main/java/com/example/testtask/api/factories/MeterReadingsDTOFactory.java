package com.example.testtask.api.factories;

import com.example.testtask.api.dto.MeterReadingsDTO;
import com.example.testtask.store.entities.MeterReadingsEntity;
import org.springframework.stereotype.Component;

/**
 * Фабрика для создания объектов DTO из сущностей показаний приборов учета.
 */
@Component
public class MeterReadingsDTOFactory {

    /**
     * Создает объект DTO для показаний прибора учета на основе сущности.
     *
     * @param entity сущность показаний прибора учета, из которой будет создан DTO
     * @return созданный объект MeterReadingsDTO
     */
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