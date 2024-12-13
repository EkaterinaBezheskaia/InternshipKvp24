package com.example.testtask.api.factories;

import com.example.testtask.api.dto.MetersDTO;
import com.example.testtask.store.entities.MetersEntity;
import org.springframework.stereotype.Component;

@Component
public class MetersDTOFactory {

    public MetersDTO makeMetersDTO(MetersEntity entity) {

        return MetersDTO.fileDtoBuilder()
                .id(entity.getId())
                .titleMetersNumber(entity.getTitleMetersNumber())
                .installationDate(entity.getInstallationDate())
                .creationDate(entity.getCreationDate())
                .build();
    }
}
