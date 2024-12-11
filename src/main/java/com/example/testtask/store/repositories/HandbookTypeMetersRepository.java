package com.example.testtask.store.repositories;

import com.example.testtask.store.entities.HandbookTypeMetersEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HandbookTypeMetersRepository extends JpaRepository<HandbookTypeMetersEntity, Long> {

    @Override
    @NonNull
    Optional<HandbookTypeMetersEntity> findById(Long id);

    @NonNull
    Page<HandbookTypeMetersEntity> findAll(Pageable pageable);

}
