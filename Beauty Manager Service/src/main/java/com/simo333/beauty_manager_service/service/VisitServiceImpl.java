package com.simo333.beauty_manager_service.service;

import com.simo333.beauty_manager_service.model.Visit;
import com.simo333.beauty_manager_service.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class VisitServiceImpl implements VisitService {

    private final VisitRepository repository;

    @Override
    public Page<Visit> getVisitsPage(Pageable page) {
        log.info("Fetching Visits. {}", page);
        return repository.findAll(page);
    }

    @Override
    public Page<Visit> getVisitsByClient(Long clientId, Pageable page) {
        log.info("Fetching Visits for client with id '{}'. {}", clientId, page);
        return repository.findAllByClientId(clientId, page);
    }

    @Override
    public Page<Visit> getVisitsByClientAndDate(Long clientId, LocalDateTime since, LocalDateTime to, Pageable page) {
        log.info("Fetching Visits for client with id '{}' and date between {} and {}. {}",
                clientId, since, to, page);
        return repository.findAllByClientIdAndDateTimeBetween(clientId, since, to, page);
    }

    @Override
    public Page<Visit> getVisitsByDate(LocalDateTime since, LocalDateTime to, Pageable page) {
        log.info("Fetching Visits with date between {} and {}. {}",
                since, to, page);
        return repository.findAllByDateTimeBetween(since, to, page);
    }

    @Transactional
    @Override
    public Visit save(Visit visit) {
        log.info("Saving a new visit with id: {}", visit.getId());
        return repository.save(visit);
    }

    @Override
    public Visit getOne(Long id) {
        Visit visit = repository.findById(id).orElseThrow(() -> {
            log.error("Visit with id '{}' not found", id);
            throw new ResourceNotFoundException("Visit not found. For id " + id);
        });
        log.info("Visit with id '{}' has been found.", visit.getId());
        return visit;
    }

    @Transactional
    @Override
    public Visit update(Visit visit) {
        getOne(visit.getId());
        log.info("Updating visit with id '{}'", visit.getId());
        return repository.save(visit);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        log.info("Deleting visit with id '{}'", id);
        repository.deleteById(id);
    }
}
