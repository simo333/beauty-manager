package com.simo333.beauty_manager_service.repository;

import com.simo333.beauty_manager_service.model.TreatmentCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class TreatmentCategoryRepositoryTest {

    @Autowired
    private TreatmentCategoryRepository repository;

    @Test
    void shouldReturnTrue() {
        // given
        String name = "name";
        TreatmentCategory category = new TreatmentCategory(null, name);

        // when
        repository.save(category);
        boolean exists = repository.existsByName(name);

        // then
        assertThat(exists).isTrue();
    }
}