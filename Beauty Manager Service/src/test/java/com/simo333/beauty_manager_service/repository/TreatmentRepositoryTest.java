package com.simo333.beauty_manager_service.repository;

import com.simo333.beauty_manager_service.model.Treatment;
import com.simo333.beauty_manager_service.model.TreatmentCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class TreatmentRepositoryTest {

    @Autowired
    private TreatmentRepository treatmentRepository;
    @Autowired
    private TreatmentCategoryRepository categoryRepository;

    @Test
    void shouldFindAllByCategoryId() {
        // given
        TreatmentCategory category = new TreatmentCategory(null, "cat");

        Treatment treatment = new Treatment();
        treatment.setName("treatment");
        treatment.setDescription("description");
        treatment.setPrice(new BigDecimal(10));
        treatment.setDuration(Duration.ofMinutes(15));
        treatment.setCategory(category);

        // when
        categoryRepository.save(category);
        treatmentRepository.save(treatment);
        List<Treatment> actualTreatments = treatmentRepository.findAllByCategoryId(category.getId());

        // then
        assertThat(actualTreatments)
                .hasSize(1)
                .contains(treatment);
    }


    @Test
    void shouldReturnTrue() {
        // given
        Treatment treatment = new Treatment();
        String name = "treatmentName";
        treatment.setName(name);
        treatment.setDescription("description");
        treatment.setPrice(new BigDecimal(10));
        treatment.setDuration(Duration.ofMinutes(15));

        // when
        treatmentRepository.save(treatment);
        boolean exists = treatmentRepository.existsByName(name);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalse() {
        // given
        String badName = "bad name";

        // when
        boolean exists = treatmentRepository.existsByName(badName);

        // then
        assertThat(exists).isFalse();
    }

}