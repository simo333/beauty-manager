package com.simo333.beauty_manager_service.controller;

import com.simo333.beauty_manager_service.dto.FreeBusyResponse;
import com.simo333.beauty_manager_service.model.Visit;
import com.simo333.beauty_manager_service.repository.VisitRepository;
import com.simo333.beauty_manager_service.service.VisitServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/visits")
@RequiredArgsConstructor
@Slf4j
public class VisitController {

    private final VisitServiceImpl service;

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<Page<Visit>> getVisits(Pageable page) {
        Page<Visit> visits = service.getVisitsPage(page);
        return new ResponseEntity<>(visits, HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/client/{id}")
    public ResponseEntity<Page<Visit>> getVisitsByClient(Pageable page, @PathVariable Long id) {
        Page<Visit> visits = service.getVisitsByClient(id, page);
        return new ResponseEntity<>(visits, HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/client/{id}/date/{since}/{to}")
    public ResponseEntity<Page<Visit>> getVisitsByClientAndDates(
            Pageable page,
            @PathVariable Long id,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        Page<Visit> visits = service.getVisitsByClientAndDate(id, since, to, page);
        return new ResponseEntity<>(visits, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/date/{since}/{to}")
    public ResponseEntity<Page<Visit>> getVisitsByDates(
            Pageable page,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        Page<Visit> visits = service.getVisitsByDate(since, to, page);
        return new ResponseEntity<>(visits, HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<Visit> getVisitById(@PathVariable Long id) {
        Visit visit = service.getOne(id);
        return new ResponseEntity<>(visit, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Visit> saveVisit(@RequestBody @Valid Visit visit) {
        log.info("visit date {}", visit.getDateTime());
        Visit saved = service.save(visit);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<Visit> updateVisit(@PathVariable Long id, @RequestBody @Valid Visit visit) {
        visit.setId(id);
        Visit updated = service.update(visit);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVisit(@PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/free-busy")
    public ResponseEntity<FreeBusyResponse> checkIfFree(@Valid @RequestBody Visit visit) {
        FreeBusyResponse response = service.checkFreeBusy(visit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
