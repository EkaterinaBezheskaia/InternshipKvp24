package com.example.testtask.api.factories;

import com.example.testtask.api.dto.MetersDTO;
import com.example.testtask.store.entities.MetersEntity;
import org.springframework.stereotype.Component;

/**
 * Фабрика для создания объектов DTO из сущностей приборов учета.
 */
@Component
public class MetersDTOFactory {

    /**
     * Создает объект DTO для прибора учета на основе сущности.
     *
     * @param entity сущность прибора учета, из которой будет создан DTO
     * @return созданный объект MetersDTO
     */
    public MetersDTO makeMetersDTO(MetersEntity entity) {
        return MetersDTO.fileDtoBuilder()
                .id(entity.getId())
                .metersSerialNumber(entity.getMetersSerialNumber())
                .installationDate(entity.getInstallationDate())
                .createdAt(entity.getCreatedAt())
                .createdAtLocal(entity.getCreatedAtLocal())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
