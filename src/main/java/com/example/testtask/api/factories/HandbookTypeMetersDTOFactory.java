package com.example.testtask.api.factories;

import com.example.testtask.api.dto.HandbookTypeMetersDTO;
import com.example.testtask.store.entities.HandbookTypeMetersEntity;
import org.springframework.stereotype.Component;

@Component
public class HandbookTypeMetersDTOFactory {

    public HandbookTypeMetersDTO makeHandbookTypeMetersDTO(HandbookTypeMetersEntity entity) {

        return HandbookTypeMetersDTO.fileDtoBuilder()
                .id(entity.getId())
                .titleTypeMeters(entity.getTitleTypeMeters())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
