package com.simo333.beauty_manager_service.service.impl;

import com.simo333.beauty_manager_service.model.TreatmentCategory;
import com.simo333.beauty_manager_service.repository.TreatmentCategoryRepository;
import com.simo333.beauty_manager_service.service.TreatmentCategoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class TreatmentCategoryServiceImplTest {

    @Autowired
    private TreatmentCategoryService categoryService;
    @Autowired
    private TreatmentCategoryRepository categoryRepository;

    @AfterEach
    void cleanUp() {
        categoryRepository.deleteAll();
    }

    @Test
    void shouldReturnList() {
        // given
        TreatmentCategory category1 = new TreatmentCategory(null, "Cat1");
        TreatmentCategory category2 = new TreatmentCategory(null, "Cat2");

        // when
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        List<TreatmentCategory> actualCategories = categoryService.getCategories();

        // then
        assertThat(actualCategories)
                .hasSize(2)
                .contains(category1, category2);

    }


    @Test
    void shouldSave() {
        // given
        TreatmentCategory category1 = new TreatmentCategory(null, "Cat1");

        // when
        TreatmentCategory saved = categoryService.save(category1);

        // then
        assertThat(saved).isEqualTo(category1);

    }

    @Test
    void shouldReturnCategory() {
        // given
        TreatmentCategory category1 = new TreatmentCategory(null, "Cat1");

        // when
        categoryRepository.save(category1);
        TreatmentCategory actualCategory = categoryService.getOne(category1.getId());

        // then
        assertThat(actualCategory).isEqualTo(category1);

    }

    @Test
    void shouldUpdate() {
        // given
        String newName = "new Name";
        TreatmentCategory category1 = new TreatmentCategory(null, "Cat1");

        // when
        categoryRepository.save(category1);
        TreatmentCategory toUpdateCategory = new TreatmentCategory(category1.getId(), newName);
        TreatmentCategory updated = categoryService.update(toUpdateCategory);

        // then
        assertThat(updated).isEqualTo(toUpdateCategory);

    }

    @Test
    void shouldDelete() {
        // given
        TreatmentCategory category1 = new TreatmentCategory(null, "Cat1");

        // when
        categoryRepository.save(category1);
        categoryService.deleteById(category1.getId());

        // then
        assertThat(categoryRepository.findById(category1.getId())).isEmpty();

    }
}