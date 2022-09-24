package com.simo333.beauty_manager_service.service;

import com.simo333.beauty_manager_service.model.Client;
import com.simo333.beauty_manager_service.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    @Override
    public Page<Client> getClientsPage(Pageable page) {
        log.info("Fetching Clients. Page: {}", page);
        return repository.findAll(page);
    }

    @Transactional
    @Override
    public Client save(Client client) {
        log.info("Saving a new client: {}", client.getFullName());
        return repository.save(client);
    }

    @Override
    public Client getOne(Long clientId) {
        Client client = repository.findById(clientId).orElseThrow(() -> {
            log.error("Client with id '{}' not found", clientId);
            throw new ResourceNotFoundException("Client not found. For id " + clientId);
        });
        log.info("Client '{}' has been found.", client.getFullName());
        return client;
    }

    @Override
    public Client getOne(String phoneNumber) {
        Client client = repository.findByPhoneNumber(phoneNumber).orElseThrow(() -> {
            log.error("Client with phone number '{}' not found", phoneNumber);
            throw new ResourceNotFoundException("Client not found. For phone number: " + phoneNumber);
        });
        log.info("Client '{}' has been found.", client.getFullName());
        return client;
    }

    @Transactional
    @Override
    public Client update(Client client) {
        getOne(client.getId());
        log.info("Updating client with id '{}'", client.getId());
        return repository.save(client);
    }

    @Transactional
    @Override
    public void deleteById(Long clientId) {
        log.info("Deleting user with id '{}'", clientId);
        repository.deleteById(clientId);
    }

    @Override
    public boolean existsByPhone(String phoneNumber) {
        if (repository.existsByPhoneNumber(phoneNumber)) {
            log.info("Phone number '{}' is already taken.", phoneNumber);
            return true;
        }
        log.info("Phone number '{}' is not taken yet.", phoneNumber);
        return false;
    }
}
