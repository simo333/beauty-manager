package com.simo333.beauty_manager_service.service;

import com.simo333.beauty_manager_service.model.Client;
import com.simo333.beauty_manager_service.security.payload.client.ClientRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {

    Page<Client> getClientsPage(Pageable page);

    List<Client> getClients();

    Client save(ClientRequest request);

    Client save(Client client);

    Client getOne(Long id);

    Client getOne(String phoneNumber);

    Client getOne(Client client);

    Client update(Long id, ClientRequest request);

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean exists(Client client);
}
