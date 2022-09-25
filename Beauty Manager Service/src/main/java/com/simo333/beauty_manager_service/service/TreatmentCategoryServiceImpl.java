package com.simo333.beauty_manager_service.service;

import com.simo333.beauty_manager_service.model.Treatment;
import com.simo333.beauty_manager_service.model.TreatmentCategory;
import com.simo333.beauty_manager_service.repository.TreatmentCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TreatmentCategoryServiceImpl implements TreatmentCategoryService {

    private final TreatmentCategoryRepository repository;
    private final TreatmentService treatmentService;

    @Override
    public List<TreatmentCategory> getCategories() {
        log.info("Fetching Treatment Categories.");
        return repository.findAll();
    }

    @Transactional
    @Override
    public TreatmentCategory save(TreatmentCategory category) {
        log.info("Saving a new treatment category: {}", category.getName());
        return repository.save(category);
    }

    @Override
    public TreatmentCategory getOne(Long id) {
        TreatmentCategory category = repository.findById(id).orElseThrow(() -> {
            log.error("TreatmentCategory with id '{}' not found", id);
            throw new ResourceNotFoundException("TreatmentCategory not found. For id " + id);
        });
        log.info("TreatmentCategory '{}' has been found.", category.getName());
        return category;
    }

    @Transactional
    @Override
    public TreatmentCategory update(TreatmentCategory category) {
        getOne(category.getId());
        log.info("Updating TreatmentCategory with id '{}'", category.getId());
        return repository.save(category);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        List<Treatment> treatments = treatmentService.getTreatmentsByCategoryId(id);
        treatments.forEach(treatment -> treatment.setCategory(null));
        log.info("Deleting TreatmentCategory with id '{}'", id);
        repository.deleteById(id);
    }
}
