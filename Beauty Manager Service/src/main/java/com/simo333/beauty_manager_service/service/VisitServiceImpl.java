package com.simo333.beauty_manager_service.service;

import com.simo333.beauty_manager_service.dto.FreeBusyResponse;
import com.simo333.beauty_manager_service.exception.FreeBusyException;
import com.simo333.beauty_manager_service.model.Visit;
import com.simo333.beauty_manager_service.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class VisitServiceImpl implements VisitService {

    private final VisitRepository repository;
    private final ClientServiceImpl clientService;

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
    public Page<Visit> getVisitsByClientAndDate(Long clientId, ZonedDateTime since, ZonedDateTime to, Pageable page) {
        log.info("Fetching Visits for client with id '{}' and date between {} and {}. {}",
                clientId, since, to, page);
        return repository.findAllByClientIdAndDateTimeBetween(clientId, since, to, page);
    }

    @Override
    public Page<Visit> getVisitsByDate(ZonedDateTime since, ZonedDateTime to, Pageable page) {
        log.info("Fetching Visits with date between {} and {}. {}",
                since, to, page);
        return repository.findAllByDateTimeBetween(since, to, page);
    }

    @Transactional
    @Override
    public Visit save(Visit visit) {
        if (visit.getClient().getId() == null) {
            if (clientService.exists(visit.getClient())) {
                visit.setClient(clientService.getOne(visit.getClient()));
            } else {
                visit.setClient(clientService.save(visit.getClient()));
            }
        }
        checkFreeBusy(visit);
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

    public FreeBusyResponse checkFreeBusy(Visit visit) {
        if (isBusy(visit)) {
            throw new FreeBusyException(getNextFreeTime(visit));
        }
        return new FreeBusyResponse(isBusy(visit), visit.getDateTime().toString());
    }

    public boolean isBusy(Visit visit) {
        ZonedDateTime treatmentFinishTime = visit.getDateTime().plus(visit.getTreatment().getDuration());
        List<Visit> allByDateTimeBetween = repository.findAllByDateTimeBetween(visit.getDateTime(), treatmentFinishTime);

        getNextFreeTime(visit);

        log.info("Check if busy: '{}' to '{}'.", visit.getDateTime(), treatmentFinishTime);
        log.info("Found visits: {}", allByDateTimeBetween.toString());
        return repository.existsByDateTimeBetween(visit.getDateTime(), treatmentFinishTime);
    }

    public String getNextFreeTime(Visit visit) {
        ZonedDateTime treatmentFinishTime = visit.getDateTime().plus(visit.getTreatment().getDuration());
        List<Visit> allByDateTimeBetween = repository.findAllByDateTimeBetween(visit.getDateTime(), treatmentFinishTime);
        Optional<Visit> lastDate = allByDateTimeBetween.stream().max(Comparator.comparing(Visit::getFinishDateTime));
        if (lastDate.isPresent()) {
            ZonedDateTime finishDateTime = lastDate.get().getFinishDateTime();
            if (finishDateTime.toLocalTime().isAfter(LocalTime.of(16, 30))) {
                return null;
            }
            return finishDateTime.toOffsetDateTime().toString();
        }
        return visit.getDateTime().toOffsetDateTime().toString();
    }
}
