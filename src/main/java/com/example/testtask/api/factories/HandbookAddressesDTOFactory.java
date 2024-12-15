package com.example.testtask.api.factories;

import com.example.testtask.api.dto.HandbookAddressesDTO;
import com.example.testtask.store.entities.HandbookAddressesEntity;
import org.springframework.stereotype.Component;

@Component
public class HandbookAddressesDTOFactory {

    public HandbookAddressesDTO makeHandbookAddressesDTO(HandbookAddressesEntity entity) {

        return HandbookAddressesDTO.fileDtoBuilder()
                .id(entity.getId())
                .street(entity.getStreet())
                .number(entity.getNumber())
                .literal(entity.getLiteral())
                .flat(entity.getFlat())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}