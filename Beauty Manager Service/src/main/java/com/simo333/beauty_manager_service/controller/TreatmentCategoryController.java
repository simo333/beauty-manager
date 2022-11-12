package com.simo333.beauty_manager_service.controller;

import com.simo333.beauty_manager_service.model.TreatmentCategory;
import com.simo333.beauty_manager_service.security.payload.treatment_category.TreatmentCategoryRequest;
import com.simo333.beauty_manager_service.service.TreatmentCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class TreatmentCategoryController {

    private final TreatmentCategoryService service;

    @GetMapping
    public ResponseEntity<Page<TreatmentCategory>> getCategories(Pageable page) {
        Page<TreatmentCategory> categories = service.getCategories(page);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TreatmentCategory>> getAllCategories() {
        List<TreatmentCategory> categories = service.getCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreatmentCategory> getCategory(@PathVariable Long id) {
        TreatmentCategory category = service.getOne(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<TreatmentCategory> saveCategory(@RequestBody @Valid TreatmentCategoryRequest request) {
        TreatmentCategory category = new TreatmentCategory(null, request.getName());
        TreatmentCategory saved = service.save(category);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<TreatmentCategory> updateCategory(@PathVariable Long id,
                                                            @RequestBody @Valid TreatmentCategoryRequest request) {
        TreatmentCategory category = new TreatmentCategory(id, request.getName());
        TreatmentCategory updated = service.update(category);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
