package com.example.testtask.store.repositories;

import com.example.testtask.store.entities.MeterReadingsEntity;
import com.example.testtask.store.entities.MetersEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Month;
import java.util.Optional;

public interface MeterReadingsRepository extends JpaRepository<MeterReadingsEntity, Long> {

    @Override
    @NonNull
    Optional<MeterReadingsEntity> findById(Long id);

    @NonNull
    Page<MeterReadingsEntity> findAll(Pageable pageable);

    Optional<MeterReadingsEntity> findByMeterAndReadingsDate(MetersEntity meter, Month readingsDate);
}
