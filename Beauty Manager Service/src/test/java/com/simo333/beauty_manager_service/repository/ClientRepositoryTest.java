package com.simo333.beauty_manager_service.repository;

import com.simo333.beauty_manager_service.model.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void shouldReturnTrue_whenExists() {
        // given
        Client client = new Client(null, "name", "lastname", "+11123456789");

        // when
        Client saved = clientRepository.save(client);
        boolean exists = clientRepository.existsById(saved.getId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalse_whenNotExists() {
        // given
        Long badId = -1L;

        // when
        boolean exists = clientRepository.existsById(badId);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void shouldReturnTrue_whenAllMatch() {
        // given
        String name = "name";
        String lastName = "lastname";
        String phone = "+11123456789";
        Client client = new Client(null, name, lastName, phone);

        // when
        clientRepository.save(client);
        boolean exists = clientRepository.existsByFirstNameAndLastNameAndPhoneNumber(name, lastName, phone);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void findByFirstNameAndLastNameAndPhoneNumber() {
        // given
        String name = "name";
        String lastName = "lastname";
        String phone = "+11123456789";
        Client client = new Client(null, name, lastName, phone);

        // when
        clientRepository.save(client);
        Optional<Client> actualClient = clientRepository.findByFirstNameAndLastNameAndPhoneNumber(name, lastName, phone);

        // then
        assertThat(actualClient).isEqualTo(Optional.of(client));
    }
}