package com.example.testtask.api.factories;

import com.example.testtask.api.dto.HandbookAddressesDTO;
import com.example.testtask.store.entities.HandbookAddressesEntity;
import org.springframework.stereotype.Component;

/**
 * Фабрика для создания объектов DTO из сущностей адресов.
 */
@Component
public class HandbookAddressesDTOFactory {

    /**
     * Создает объект DTO для адреса на основе сущности.
     *
     * @param entity сущность адреса, из которой будет создан DTO
     * @return созданный объект HandbookAddressesDTO
     */
    public HandbookAddressesDTO makeHandbookAddressesDTO(HandbookAddressesEntity entity) {
        return HandbookAddressesDTO.fileDtoBuilder()
                .id(entity.getId())
                .street(entity.getStreet())
                .number(entity.getNumber())
                .literal(entity.getLiteral())
                .flat(entity.getFlat())
                .createdAt(entity.getCreatedAt())
                .createdAtLocal(entity.getCreatedAtLocal())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}