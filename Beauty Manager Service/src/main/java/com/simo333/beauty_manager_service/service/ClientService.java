package com.simo333.beauty_manager_service.service;

import com.simo333.beauty_manager_service.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {

    Page<Client> getClientsPage(Pageable page);

    Client save(Client client);

    Client getOne(Long id);

    Client getOne(String phoneNumber);

    Client update(Client client);

    void deleteById(Long id);

    boolean existsByPhone(String phoneNumber);
}
