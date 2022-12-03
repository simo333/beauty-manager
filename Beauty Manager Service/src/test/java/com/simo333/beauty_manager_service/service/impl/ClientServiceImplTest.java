package com.simo333.beauty_manager_service.service.impl;

import com.simo333.beauty_manager_service.model.Client;
import com.simo333.beauty_manager_service.repository.ClientRepository;
import com.simo333.beauty_manager_service.service.ClientService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ClientServiceImplTest {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository clientRepository;

    @AfterEach
    void cleanUp() {
        clientRepository.deleteAll();
    }

    @Test
    void shouldReturnClients() {
        // given
        Client client1 = Client.builder()
                .phoneNumber("+11123456789")
                .firstName("name")
                .lastName("lastName")
                .build();

        Client client2 = Client.builder()
                .phoneNumber("+11123456781")
                .firstName("name1")
                .lastName("lastName2")
                .build();

        // when
        clientRepository.save(client1);
        clientRepository.save(client2);
        List<Client> actualClients = clientService.getClients();

        // then
        assertThat(actualClients)
                .hasSize(2)
                .contains(client1, client2);

    }

    @Test
    void shouldSetClientId_whenSaved() {
        // given
        Client client = Client.builder()
                .phoneNumber("+11123456789")
                .firstName("name")
                .lastName("lastName")
                .build();

        // when
        clientService.save(client);

        // then
        assertThat(client.getId()).isNotNull();
    }

    @Test
    void getOne() {
        // given
        Client client = Client.builder()
                .phoneNumber("+11123456789")
                .firstName("name")
                .lastName("lastName")
                .build();

        // when
        clientRepository.save(client);
        Client actualClient = clientService.getOne(client.getId());

        // then
        assertThat(actualClient).isEqualTo(client);

    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void existsById() {
    }

    @Test
    void exists() {
    }
}