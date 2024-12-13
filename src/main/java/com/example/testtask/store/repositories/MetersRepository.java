package com.example.testtask.store.repositories;

import com.example.testtask.store.entities.MetersEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetersRepository extends JpaRepository<MetersEntity, Long> {

    @Override
    @NonNull
    Optional<MetersEntity> findById(Long id);

    @NonNull
    Page<MetersEntity> findAll(Pageable pageable);

    Optional<MetersEntity> findByTitleMetersNumber(String titleMetersNumber);

}
