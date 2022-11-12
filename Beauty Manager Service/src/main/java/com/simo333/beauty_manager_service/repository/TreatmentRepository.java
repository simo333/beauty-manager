package com.simo333.beauty_manager_service.repository;

import com.simo333.beauty_manager_service.model.Treatment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    List<Treatment> findAllByCategoryId(Long categoryId);

    Page<Treatment> findAllByCategoryId(Long categoryId, Pageable page);

    boolean existsByName(String name);
}
