package com.example.testtask.store.repositories;

import com.example.testtask.store.entities.HandbookMeterTypesEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностями типов приборов учета.
 * Наследует функциональность от JpaRepository.
 */
public interface HandbookMeterTypesRepository extends JpaRepository<HandbookMeterTypesEntity, Long> {

    /**
     * Находит тип прибора учета по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор типа прибора учета
     * @return Optional с найденным типом прибора учета или пустой Optional, если тип не найден
     */
    @Override
    @NonNull
    Optional<HandbookMeterTypesEntity> findById(@NonNull Long id);

    /**
     * Находит все типы приборов учета с поддержкой пагинации.
     *
     * @param pageable объект, содержащий информацию о пагинации
     * @return страница с типами приборов учета
     */
    @NonNull
    Page<HandbookMeterTypesEntity> findAll(@NonNull Pageable pageable);

    /**
     * Находит тип прибора учета по его названию.
     *
     * @param meterTypeTitle название типа прибора учета
     * @return Optional с найденным типом прибора учета или пустой Optional, если тип не найден
     */
    Optional<HandbookMeterTypesEntity> findByMeterTypeTitle(String meterTypeTitle);
}
