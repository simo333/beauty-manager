package com.simo333.beauty_manager_service.service;

import com.simo333.beauty_manager_service.model.TreatmentCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TreatmentCategoryService {
    Page<TreatmentCategory> getCategories(Pageable page);

    List<TreatmentCategory> getCategories();

    TreatmentCategory save(TreatmentCategory category);

    TreatmentCategory getOne(Long id);

    TreatmentCategory update(TreatmentCategory category);

    void deleteById(Long id);
}
