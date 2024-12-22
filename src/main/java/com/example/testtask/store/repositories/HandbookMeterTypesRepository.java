package com.example.testtask.store.repositories;

import com.example.testtask.store.entities.HandbookMeterTypesEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HandbookMeterTypesRepository extends JpaRepository<HandbookMeterTypesEntity, Long> {

    @Override
    @NonNull
    Optional<HandbookMeterTypesEntity> findById(@NonNull Long id);

    @NonNull
    Page<HandbookMeterTypesEntity> findAll(@NonNull Pageable pageable);

    Optional<HandbookMeterTypesEntity> findByMeterTypeTitle(String meterTypeTitle);
}
