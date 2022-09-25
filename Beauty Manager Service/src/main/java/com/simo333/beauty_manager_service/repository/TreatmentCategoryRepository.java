package com.simo333.beauty_manager_service.repository;

import com.simo333.beauty_manager_service.model.TreatmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentCategoryRepository extends JpaRepository<TreatmentCategory, Long> {
}
