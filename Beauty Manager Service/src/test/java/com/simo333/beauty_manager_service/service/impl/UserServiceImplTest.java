package com.simo333.beauty_manager_service.service.impl;

import com.simo333.beauty_manager_service.model.Client;
import com.simo333.beauty_manager_service.model.Role;
import com.simo333.beauty_manager_service.model.User;
import com.simo333.beauty_manager_service.repository.ClientRepository;
import com.simo333.beauty_manager_service.repository.RoleRepository;
import com.simo333.beauty_manager_service.repository.UserRepository;
import com.simo333.beauty_manager_service.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ClientRepository clientRepository;

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        clientRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void shouldReturnUsers() {
        // given
        User user = new User();
        Role roleUser = new Role(null, Role.Type.ROLE_USER);
        Client client = new Client(null, "name", "lastName", "+11123456789");
        user.setRoles(Set.of(roleUser));
        user.setPassword("password");
        user.setEmail("mail@mail.com");
        user.setClient(client);


        // when
        roleRepository.save(roleUser);
        clientRepository.save(client);
        userRepository.save(user);
        Page<User> actualUsers = userService.getUsersPage(PageRequest.of(0, 20));

        // then
        assertThat(actualUsers)
                .hasSize(1)
                .contains(user);

    }

    @Test
    void shouldReturnUserByEmail() {
        // given
        String email = "mail@mail.com";
        User user = new User();
        Role roleUser = new Role(null, Role.Type.ROLE_USER);
        Client client = new Client(null, "name", "lastName", "+11123456789");
        user.setRoles(Set.of(roleUser));
        user.setPassword("password");
        user.setEmail(email);
        user.setClient(client);

        // when
        roleRepository.save(roleUser);
        clientRepository.save(client);
        userRepository.save(user);
        User actualUser = userService.getUser(email);

        // then
        assertThat(actualUser).isEqualTo(user);

    }

    @Test
    void shouldReturnUserById() {
        // given
        User user = new User();
        Role roleUser = new Role(null, Role.Type.ROLE_USER);
        Client client = new Client(null, "name", "lastName", "+11123456789");
        user.setRoles(Set.of(roleUser));
        user.setPassword("password");
        user.setEmail("mail@mail.com");
        user.setClient(client);

        // when
        roleRepository.save(roleUser);
        clientRepository.save(client);
        userRepository.save(user);
        User actualUser = userService.getUser(user.getId());

        // then
        assertThat(actualUser).isEqualTo(user);

    }

    @Test
    void shouldReturnTrue() {
        // given
        String email = "mail@mail.com";
        User user = new User();
        Role roleUser = new Role(null, Role.Type.ROLE_USER);
        Client client = new Client(null, "name", "lastName", "+11123456789");
        user.setRoles(Set.of(roleUser));
        user.setPassword("password");

        user.setEmail(email);
        user.setClient(client);

        // when
        roleRepository.save(roleUser);
        clientRepository.save(client);
        userRepository.save(user);
        boolean exists = userService.existsByEmail(email);

        // then
        assertThat(exists).isTrue();

    }

}