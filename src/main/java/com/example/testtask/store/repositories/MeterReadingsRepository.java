package com.example.testtask.store.repositories;

import com.example.testtask.store.entities.MeterReadingsEntity;
import com.example.testtask.store.entities.MetersEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностями показаний приборов учета.
 * Наследует функциональность от JpaRepository.
 */
public interface MeterReadingsRepository extends JpaRepository<MeterReadingsEntity, Long> {

    /**
     * Находит показания прибора учета по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор показаний
     * @return Optional с найденными показаниями или пустой Optional, если показания не найдены
     */
    @Override
    @NonNull
    Optional<MeterReadingsEntity> findById(@NonNull Long id);

    /**
     * Находит все показания приборов учета с поддержкой пагинации.
     *
     * @param pageable объект, содержащий информацию о пагинации
     * @return страница с показаниями приборов учета
     */
    @NonNull
    Page<MeterReadingsEntity> findAll(@NonNull Pageable pageable);

    /**
     * Находит показания прибора учета по прибору и дате показаний.
     *
     * @param metersSerialNumber прибор учета
     * @param readingsDate дата показаний
     * @return Optional с найденными показаниями или пустой Optional, если показания не найдены
     */
    Optional<MeterReadingsEntity> findByMeter_MetersSerialNumberAndReadingsDate(String metersSerialNumber, Month readingsDate);

    Optional<Object> findByMeter_MetersSerialNumberAndReadingsAndReadingsDate(String metersSerialNumber, BigDecimal newReadings, Month readingsDate);

    List<MeterReadingsEntity> findByMeter_MetersSerialNumberOrderByReadingsDateAsc(String metersSerialNumber, Pageable pageable);
}
