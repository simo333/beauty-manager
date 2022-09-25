package com.simo333.beauty_manager_service.service;

import com.simo333.beauty_manager_service.model.TreatmentCategory;

import java.util.List;

public interface TreatmentCategoryService {
    List<TreatmentCategory> getCategories();

    TreatmentCategory save(TreatmentCategory category);

    TreatmentCategory getOne(Long id);

    TreatmentCategory update(TreatmentCategory category);

    void deleteById(Long id);
}
