package com.example.testtask.store.repositories;

import com.example.testtask.store.entities.HandbookAddressesEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;

import java.util.List;
import java.util.Optional;

public interface HandbookAddressesRepository extends JpaRepository<HandbookAddressesEntity, Long> {

//    @Override
    @NonNull
    Optional<HandbookAddressesEntity> findByTitleAddress(String titleAddress);

    @Override
    @NonNull
    Page<HandbookAddressesEntity> findAll(Pageable pageable);

    Optional<HandbookAddressesEntity> findByTitleAddressAndLiteral(String titleAddress, String literal);

    List<HandbookAddressesEntity> findByTitleAddressOrderByLiteralAsc(PageRequest of);
}
