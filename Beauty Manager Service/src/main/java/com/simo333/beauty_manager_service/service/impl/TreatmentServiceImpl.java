package com.simo333.beauty_manager_service.service.impl;

import com.simo333.beauty_manager_service.exception.NotAvailableException;
import com.simo333.beauty_manager_service.model.Treatment;
import com.simo333.beauty_manager_service.repository.TreatmentRepository;
import com.simo333.beauty_manager_service.security.payload.treatment.TreatmentRequest;
import com.simo333.beauty_manager_service.service.TreatmentService;
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
    public List<Treatment> getTreatments() {
        log.info("Fetching Treatments.");
        return repository.findAll();
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
    public Treatment save(TreatmentRequest request) {
        checkNameAvailable(request.getName());
        Treatment treatment = buildTreatment(request);
        log.info("Saving a new treatment: {}", treatment.getName());
        return repository.save(treatment);
    }

    @Transactional
    @Override
    public Treatment save(Treatment treatment) {
        checkNameAvailable(treatment.getName());
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
    public Treatment update(Long id, TreatmentRequest request) {
        Treatment one = getOne(id);
        if (!one.getName().equals(request.getName())) {
            checkNameAvailable(request.getName());
        }
        Treatment treatment = buildTreatment(request);
        treatment.setId(id);
        log.info("Updating treatment with id '{}'", treatment.getId());
        return repository.save(treatment);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        log.info("Deleting treatment with id '{}'", id);
        repository.deleteById(id);
    }

    private void checkNameAvailable(String name) {
        if (repository.existsByName(name)) {
            throw new NotAvailableException(String.format("Treatment with name %s already exists.", name));
        }
    }

    private Treatment buildTreatment(TreatmentRequest request) {
        return Treatment.builder()
                .name(request.getName())
                .description(request.getDescription())
                .duration(request.getDuration())
                .price(request.getPrice())
                .category(request.getCategory())
                .build();
    }
}
