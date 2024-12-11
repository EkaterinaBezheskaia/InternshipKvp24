package com.example.testtask.store.repositories;

import com.example.testtask.store.entities.HandbookAddressesEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HandbookAddressesRepository extends JpaRepository<HandbookAddressesEntity, Long> {

    @Override
    @NonNull
    Optional<HandbookAddressesEntity> findById(Long id);

    @NonNull
    Page<HandbookAddressesEntity> findAll(Pageable pageable);

}
