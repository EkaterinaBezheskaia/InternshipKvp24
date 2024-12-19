package com.example.testtask.store.repositories;

import com.example.testtask.store.entities.HandbookAddressesEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HandbookAddressesRepository extends JpaRepository<HandbookAddressesEntity, Long> {

    @NonNull
    Optional<HandbookAddressesEntity> findByStreet(String street);

    @Override
    @NonNull
    Page<HandbookAddressesEntity> findAll(@NonNull Pageable pageable);

    @Query("SELECT a FROM HandbookAddressesEntity a WHERE a.street = :street AND a.number = :number " +
            "AND (:literal IS NULL OR a.literal = :literal) " +
            "AND (:flat IS NULL OR a.flat = :flat)")
    Optional<HandbookAddressesEntity> findByStreetAndNumberAndLiteralAndFlat (String street, Integer number, String literal, Integer flat);

    @Query("SELECT a FROM HandbookAddressesEntity a WHERE a.street = :street AND a.number = :number " +
            "AND (:literal IS NULL OR a.literal = :literal) ")
    Optional<HandbookAddressesEntity> findByStreetAndNumberAndLiteral (String street, Integer number, String literal);

    List<HandbookAddressesEntity> findByStreetOrderByNumberAsc(String street, Pageable pageable);

    List<HandbookAddressesEntity> findByStreetAndNumber(String street, Integer number);

    List<HandbookAddressesEntity> findByStreetAndNumberOrderByLiteralAscFlatAsc(
            String street,
            Integer number,
            Pageable pageable);
}
