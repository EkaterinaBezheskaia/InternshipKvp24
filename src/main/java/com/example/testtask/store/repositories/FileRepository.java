package com.example.testtask.store.repositories;

import com.example.testtask.store.entities.FileEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    @Override
    @NonNull
    Optional<FileEntity> findById(Long id);

    @NonNull
    Page<FileEntity> findAll(Pageable pageable);

}
