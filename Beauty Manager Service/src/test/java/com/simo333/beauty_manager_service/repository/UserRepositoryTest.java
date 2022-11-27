package com.simo333.beauty_manager_service.repository;

import com.simo333.beauty_manager_service.model.Client;
import com.simo333.beauty_manager_service.model.Role;
import com.simo333.beauty_manager_service.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Test
    void shouldFindUser() {
        // given
        Client client = new Client(null, "name", "lastName", "+11123456789");
        Role role = new Role(null, Role.Type.ROLE_USER);
        String email = "email@mail.pl";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");
        user.setClient(client);
        user.setRoles(Set.of(role));

        // when
        roleRepository.save(role);
        clientRepository.save(client);
        userRepository.save(user);
        Optional<User> actualEmail = userRepository.findByEmail(email);

        // then
        assertThat(actualEmail).isEqualTo(Optional.of(user));

    }

    @Test
    void shouldReturnTrue() {
        // given
        Client client = new Client(null, "name", "lastName", "+11123456789");
        Role role = new Role(null, Role.Type.ROLE_ADMIN);
        String email = "mail@mail.pl";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");
        user.setClient(client);
        user.setRoles(Set.of(role));

        // when
        roleRepository.save(role);
        clientRepository.save(client);
        userRepository.save(user);
        boolean exists = userRepository.existsByEmail(email);

        // then
        assertThat(exists).isTrue();
    }
}