package com.example.testtask.api.factories;

import com.example.testtask.api.dto.MetersDTO;
import com.example.testtask.store.entities.MetersEntity;
import org.springframework.stereotype.Component;

@Component
public class MetersDTOFactory {

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
