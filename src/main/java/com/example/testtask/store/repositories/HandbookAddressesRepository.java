package com.example.testtask.store.repositories;

import com.example.testtask.store.entities.HandbookAddressesEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностями адресов в справочнике.
 * Наследует функциональность от JpaRepository.
 */
public interface HandbookAddressesRepository extends JpaRepository<HandbookAddressesEntity, Long> {

    /**
     * Находит все адреса с поддержкой пагинации.
     *
     * @param pageable объект, содержащий информацию о пагинации
     * @return страница с адресами
     */
    @Override
    @NonNull
    Page<HandbookAddressesEntity> findAll(@NonNull Pageable pageable);

    /**
     * Находит адрес по улице, номеру, литералу и квартире.
     * Если литерал или квартира равны null, они игнорируются в поиске.
     *
     * @param street улица
     * @param number номер дома
     * @param literal литерал (может быть null)
     * @param flat номер квартиры (может быть null)
     * @return найденный адрес или пустой Optional, если адрес не найден
     */
    @Query("SELECT a FROM HandbookAddressesEntity a WHERE a.street = :street AND a.number = :number " +
            "AND (:literal IS NULL OR a.literal = :literal) " +
            "AND (:flat IS NULL OR a.flat = :flat)")
    Optional<HandbookAddressesEntity> findByStreetAndNumberAndLiteralAndFlat(String street, Integer number, String literal, Integer flat);

    /**
     * Находит адреса по улице и сортирует их по номеру в порядке возрастания.
     *
     * @param street улица
     * @param pageable объект, содержащий информацию о пагинации
     * @return список адресов
     */
    List<HandbookAddressesEntity> findByStreetOrderByNumberAscLiteralAscNumberAsc(String street, Pageable pageable);

    /**
     * Находит адреса по улице и номеру, сортируя их по литералу и квартире в порядке возрастания.
     *
     * @param street улица
     * @param number номер дома
     * @param pageable объект, содержащий информацию о пагинации
     * @return список адресов
     */
    List<HandbookAddressesEntity> findByStreetAndNumberOrderByLiteralAscFlatAsc(
            String street,
            Integer number,
            Pageable pageable);

    /**
     * Проверяет, существует ли адрес с заданной улицей, номером, литералом и квартирой.
     *
     * @param street улица
     * @param number номер дома
     * @param literal литерал (может быть null)
     * @param flat номер квартиры (может быть null)
     * @return true, если адрес существует, иначе false
     */
    @Query("SELECT COUNT(a) > 0 FROM HandbookAddressesEntity a WHERE a.street = :street AND a.number = :number " +
            "AND (:literal IS NULL OR a.literal = :literal) " +
            "AND (:flat IS NULL OR a.flat = :flat)")
    boolean existsByStreetAndNumberAndLiteralAndFlat(String street, Integer number, String literal, Integer flat);


}
