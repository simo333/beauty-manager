package com.simo333.beauty_manager_service.service.impl;

import com.simo333.beauty_manager_service.model.Treatment;
import com.simo333.beauty_manager_service.model.TreatmentCategory;
import com.simo333.beauty_manager_service.repository.TreatmentCategoryRepository;
import com.simo333.beauty_manager_service.repository.TreatmentRepository;
import com.simo333.beauty_manager_service.service.TreatmentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class TreatmentServiceImplTest {

    @Autowired
    private TreatmentService treatmentService;
    @Autowired
    private TreatmentRepository treatmentRepository;
    @Autowired
    private TreatmentCategoryRepository categoryRepository;

    @AfterEach
    void cleanUp() {
        treatmentRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void shouldReturnList() {
        // given
        Treatment treatment1 = new Treatment();
        treatment1.setName("treatment1");
        treatment1.setPrice(BigDecimal.TEN);
        treatment1.setDescription("description");
        treatment1.setDuration(Duration.of(15, ChronoUnit.MINUTES));

        Treatment treatment2 = new Treatment();
        treatment2.setName("treatment2");
        treatment2.setPrice(BigDecimal.TEN);
        treatment2.setDescription("description");
        treatment2.setDuration(Duration.of(15, ChronoUnit.MINUTES));

        // when
        treatmentRepository.save(treatment1);
        treatmentRepository.save(treatment2);
        List<Treatment> actualTreatments = treatmentService.getTreatments();

        // then
        assertThat(actualTreatments)
                .hasSize(2)
                .contains(treatment1, treatment2);

    }

    @Test
    void shouldReturnList_byCategoryId() {
        // given
        String categoryName = "category";
        TreatmentCategory category = new TreatmentCategory(null, categoryName);

        Treatment treatment1 = new Treatment();
        treatment1.setName("treatment1");
        treatment1.setPrice(BigDecimal.TEN);
        treatment1.setDescription("description");
        treatment1.setDuration(Duration.of(15, ChronoUnit.MINUTES));
        treatment1.setCategory(category);

        // when
        categoryRepository.save(category);
        treatmentRepository.save(treatment1);
        List<Treatment> actualTreatments = treatmentService.getTreatmentsByCategoryId(category.getId());

        // then
        assertThat(actualTreatments)
                .hasSize(1)
                .contains(treatment1);

    }

    @Test
    void shouldSave() {
        // given
        Treatment treatment1 = new Treatment();
        treatment1.setName("treatment1");
        treatment1.setPrice(BigDecimal.TEN);
        treatment1.setDescription("description");
        treatment1.setDuration(Duration.of(15, ChronoUnit.MINUTES));

        // when
        Treatment saved = treatmentService.save(treatment1);

        // then
        assertThat(saved).isEqualTo(treatment1);

    }

    @Test
    void shouldReturnOne() {
        // given
        Treatment treatment1 = new Treatment();
        treatment1.setName("treatment1");
        treatment1.setPrice(BigDecimal.TEN);
        treatment1.setDescription("description");
        treatment1.setDuration(Duration.of(15, ChronoUnit.MINUTES));

        // when
        treatmentRepository.save(treatment1);
        Treatment actualTreatment = treatmentService.getOne(treatment1.getId());

        // then
        assertThat(actualTreatment).isEqualTo(treatment1);

    }

    @Test
    void shouldDelete() {
        // given
        Treatment treatment1 = new Treatment();
        treatment1.setName("treatment1");
        treatment1.setPrice(BigDecimal.TEN);
        treatment1.setDescription("description");
        treatment1.setDuration(Duration.of(15, ChronoUnit.MINUTES));

        // when
        treatmentRepository.save(treatment1);
        treatmentService.deleteById(treatment1.getId());

        // then
        assertThat(treatmentRepository.findById(treatment1.getId())).isEmpty();

    }
}