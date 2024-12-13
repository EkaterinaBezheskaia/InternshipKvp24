package com.example.testtask.store.repositories;

import com.example.testtask.store.entities.HandbookAddressesEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HandbookAddressesRepository extends JpaRepository<HandbookAddressesEntity, Long> {

    @NonNull
    Optional<HandbookAddressesEntity> findByTitleAddress(String titleAddress);

    @Override
    @NonNull
    Page<HandbookAddressesEntity> findAll(@NonNull Pageable pageable);

    Optional<HandbookAddressesEntity> findByTitleAddressAndNumberAndLiteral (String titleAddress, Integer number, String literal);

    List<HandbookAddressesEntity> findByTitleAddressOrderByNumberAsc(PageRequest of);
}
