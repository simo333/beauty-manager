package com.simo333.beauty_manager_service.service;

import com.simo333.beauty_manager_service.model.Treatment;
import com.simo333.beauty_manager_service.repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository repository;

    @Override
    public Page<Treatment> getTreatmentsPage(Pageable page) {
        log.info("Fetching Treatments. {}", page);
        return repository.findAll(page);
    }

    @Override
    public Page<Treatment> getTreatmentsByCategoryId(Long categoryId, Pageable page) {
        log.info("Fetching Treatments with category: {}. {}", categoryId, page);
        return repository.findAllByCategoryId(categoryId, page);
    }

    @Override
    public List<Treatment> getTreatmentsByCategoryId(Long categoryId) {
        log.info("Fetching Treatments with category: {}.", categoryId);
        return repository.findAllByCategoryId(categoryId);
    }

    @Transactional
    @Override
    public Treatment save(Treatment treatment) {
        log.info("Saving a new treatment: {}", treatment.getName());
        return repository.save(treatment);
    }

    @Override
    public Treatment getOne(Long id) {
        Treatment treatment = repository.findById(id).orElseThrow(() -> {
            log.error("Treatment with id '{}' not found", id);
            throw new ResourceNotFoundException("Treatment not found. For id " + id);
        });
        log.info("Treatment '{}' has been found.", treatment.getName());
        return treatment;
    }

    @Transactional
    @Override
    public Treatment update(Treatment treatment) {
        getOne(treatment.getId());
        log.info("Updating treatment with id '{}'", treatment.getId());
        return repository.save(treatment);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        log.info("Deleting treatment with id '{}'", id);
        repository.deleteById(id);
    }
}
