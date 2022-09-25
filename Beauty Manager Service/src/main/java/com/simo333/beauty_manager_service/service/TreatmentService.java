package com.simo333.beauty_manager_service.service;

import com.simo333.beauty_manager_service.model.Treatment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TreatmentService {
    Page<Treatment> getTreatmentsPage(Pageable page);

    Page<Treatment> getTreatmentsByCategoryId(Long categoryId, Pageable page);

    List<Treatment> getTreatmentsByCategoryId(Long categoryId);

    Treatment save(Treatment treatment);

    Treatment getOne(Long id);

    Treatment update(Treatment treatment);

    void deleteById(Long id);
}
