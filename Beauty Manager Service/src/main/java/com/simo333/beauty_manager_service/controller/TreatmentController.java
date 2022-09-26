package com.simo333.beauty_manager_service.controller;

import com.simo333.beauty_manager_service.model.Treatment;
import com.simo333.beauty_manager_service.service.TreatmentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/treatments")
@RequiredArgsConstructor
@Slf4j
public class TreatmentController {

    private final TreatmentServiceImpl service;

    @GetMapping
    public ResponseEntity<Page<Treatment>> getTreatments(Pageable page) {
        Page<Treatment> treatments = service.getTreatmentsPage(page);
        return new ResponseEntity<>(treatments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Treatment> getTreatmentById(@PathVariable Long id) {
        Treatment treatment = service.getOne(id);
        return new ResponseEntity<>(treatment, HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<Treatment>> getTreatmentByCategory(@PathVariable Long id) {
        List<Treatment> treatments = service.getTreatmentsByCategoryId(id);
        return new ResponseEntity<>(treatments, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<Treatment> saveTreatment(@RequestBody @Valid Treatment treatment) {
        Treatment saved = service.save(treatment);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<Treatment> updateTreatment(@PathVariable Long id, @RequestBody @Valid Treatment treatment) {
        treatment.setId(id);
        Treatment updated = service.update(treatment);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTreatment(@PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
