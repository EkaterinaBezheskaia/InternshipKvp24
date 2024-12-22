package com.example.testtask.store.repositories;

import com.example.testtask.store.entities.MetersEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностями приборов учета.
 * Наследует функциональность от JpaRepository.
 */
public interface MetersRepository extends JpaRepository<MetersEntity, Long> {

    /**
     * Находит прибор учета по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор прибора учета
     * @return Optional с найденным прибором учета или пустой Optional, если прибор не найден
     */
    @Override
    @NonNull
    Optional<MetersEntity> findById(@NonNull Long id);

    /**
     * Находит все приборы учета с поддержкой пагинации.
     *
     * @param pageable объект, содержащий информацию о пагинации
     * @return страница с приборами учета
     */
    @NonNull
    Page<MetersEntity> findAll(@NonNull Pageable pageable);

    /**
     * Находит прибор учета по его серийному номеру.
     *
     * @param metersSerialNumber серийный номер прибора учета
     * @return Optional с найденным прибором учета или пустой Optional, если прибор не найден
     */
    Optional<MetersEntity> findByMetersSerialNumber(String metersSerialNumber);

    /**
     * Находит прибор учета по его серийному номеру и дате установки.
     *
     * @param metersSerialNumber серийный номер прибора учета
     * @param installationDate дата установки прибора учета
     * @return Optional с найденным прибором учета или пустой Optional, если прибор не найден
     */
    Optional<MetersEntity> findByMetersSerialNumberAndInstallationDate(String metersSerialNumber, LocalDate installationDate);
}
