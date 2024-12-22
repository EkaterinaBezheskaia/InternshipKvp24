package com.example.testtask.api.factories;

import com.example.testtask.api.dto.HandbookMeterTypesDTO;
import com.example.testtask.store.entities.HandbookMeterTypesEntity;
import org.springframework.stereotype.Component;

/**
 * Фабрика для создания объектов DTO из сущностей типов приборов учета.
 */
@Component
public class HandbookMeterTypesDTOFactory {

    /**
     * Создает объект DTO для типа прибора учета на основе сущности.
     *
     * @param entity сущность типа прибора учета, из которой будет создан DTO
     * @return созданный объект HandbookMeterTypesDTO
     */
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
