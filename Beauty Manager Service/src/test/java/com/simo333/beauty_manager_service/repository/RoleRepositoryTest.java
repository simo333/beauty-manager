package com.simo333.beauty_manager_service.repository;

import com.simo333.beauty_manager_service.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldFindRole() {
        // given
        Role expectedRole = new Role();
        expectedRole.setName(Role.Type.ROLE_USER);

        // when
        roleRepository.save(expectedRole);
        Optional<Role> actualRole = roleRepository.findByName(Role.Type.ROLE_USER);

        // then
        assertThat(actualRole).isEqualTo(Optional.of(expectedRole));
    }
}