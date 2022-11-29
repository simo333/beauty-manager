package com.simo333.beauty_manager_service.service.impl;

import com.simo333.beauty_manager_service.model.Role;
import com.simo333.beauty_manager_service.model.User;
import com.simo333.beauty_manager_service.repository.RoleRepository;
import com.simo333.beauty_manager_service.service.RoleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class RoleServiceImplTest {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleRepository roleRepository;

    @AfterEach
    void cleanUp() {
        roleRepository.deleteAll();
    }

    @Test
    void shouldReturnAllRoles() {
        // given
        Role roleUser = new Role(null, Role.Type.ROLE_USER);
        Role roleAdmin = new Role(null, Role.Type.ROLE_ADMIN);

        // when
        roleRepository.saveAll(List.of(roleUser, roleAdmin));
        List<Role> actualRoles = roleService.getAll();

        // then
        assertThat(actualRoles)
                .hasSize(2)
                .contains(roleAdmin, roleUser);
    }

    @Test
    void shouldReturnRoleByType() {
        // given
        Role roleUser = new Role(null, Role.Type.ROLE_USER);

        // when
        roleRepository.save(roleUser);
        Role actualRole = roleService.getRole(Role.Type.ROLE_USER);

        // then
        assertThat(actualRole).isEqualTo(roleUser);
    }

    @Test
    void shouldThrow() {
        // given
        Role.Type wrongRole = Role.Type.ROLE_USER;

        // then
        assertThatThrownBy(() -> roleService.getRole(wrongRole))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Role not found");
    }

    @Test
    void shouldReturnRoleById() {
        // given
        Role roleUser = new Role(null, Role.Type.ROLE_USER);

        // when
        roleRepository.save(roleUser);
        Role actualRole = roleService.getRole(roleUser.getId());

        // then
        assertThat(actualRole).isEqualTo(roleUser);
    }

    @Test
    void save() {
        // given
        Role roleUser = new Role(null, Role.Type.ROLE_USER);

        // when
        roleService.save(roleUser);

        // then
        assertThat(roleUser.getId()).isNotNull();
    }


    @Test
    void deleteById() {
        // given
        Role roleUser = new Role(null, Role.Type.ROLE_USER);

        // when
        roleRepository.save(roleUser);
        roleService.deleteById(roleUser.getId());
        Optional<Role> actualUser = roleRepository.findById(roleUser.getId());

        // then
        assertThat(actualUser).isEmpty();
    }
}