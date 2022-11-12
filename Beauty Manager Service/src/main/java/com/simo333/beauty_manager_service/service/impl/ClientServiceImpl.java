package com.simo333.beauty_manager_service.service.impl;

import com.simo333.beauty_manager_service.exception.NotAvailableException;
import com.simo333.beauty_manager_service.model.Client;
import com.simo333.beauty_manager_service.repository.ClientRepository;
import com.simo333.beauty_manager_service.security.payload.client.ClientRequest;
import com.simo333.beauty_manager_service.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    @Override
    public Page<Client> getClientsPage(Pageable page) {
        log.info("Fetching Clients. {}", page);
        return repository.findAll(page);
    }

    @Override
    public List<Client> getClients() {
        log.info("Fetching Clients.");
        return repository.findAll();
    }

    @Transactional
    @Override
    public Client save(ClientRequest request) {
        if (checkAvailable(request)) {
            return repository.save(buildClient(request));
        }
        throw new NotAvailableException("Client with these parameters already exists.");
    }

    @Transactional
    @Override
    public Client save(Client client) {
        if (checkAvailable(client)) {
            return repository.save(client);
        }
        throw new NotAvailableException("Client with these parameters already exists.");
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
        Client client = repository.findDistinctByPhoneNumber(phoneNumber).orElseThrow(() -> {
            log.error("Client with phone number '{}' not found", phoneNumber);
            throw new ResourceNotFoundException("Client not found. For phone number: " + phoneNumber);
        });
        log.info("Client '{}' has been found.", client.getFullName());
        return client;
    }

    @Override
    public Client getOne(Client client) {
        Client found = repository.findByFirstNameAndLastNameAndPhoneNumber(
                client.getFirstName(), client.getLastName(), client.getPhoneNumber()).orElseThrow(() -> {
            log.error("Client with '{}' not found", client);
            throw new ResourceNotFoundException("Client not found. For client: " + client);
        });
        log.info("Client '{}' has been found.", client.getFullName());
        return found;
    }

    @Transactional
    @Override
    public Client update(Long id, ClientRequest request) {
        getOne(id);
        if (checkAvailable(request)) {
            Client client = buildClient(request);
            client.setId(id);
            log.info("Updating client with id '{}'", id);
            return repository.save(client);
        }
        throw new NotAvailableException("Client with these parameters already exists.");
    }

    @Transactional
    @Override
    public void deleteById(Long clientId) {
        log.info("Deleting client with id '{}'", clientId);
        repository.deleteById(clientId);
    }

    @Override
    public boolean existsById(Long id) {
        if (repository.existsById(id)) {
            log.info("Client with id '{}' exists.", id);
            return true;
        }
        return false;
    }

    @Override
    public boolean exists(Client client) {
        if (repository.existsByFirstNameAndLastNameAndPhoneNumber(
                client.getFirstName(), client.getLastName(), client.getPhoneNumber())) {
            log.info("Client with full name '{}' and phone number '{}' exists.",
                    client.getFullName(), client.getPhoneNumber());
            return true;
        }
        return false;
    }

    private Client buildClient(ClientRequest request) {
        return Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getFirstName())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }

    private boolean checkAvailable(ClientRequest request) {
        return !repository.existsByFirstNameAndLastNameAndPhoneNumber(
                request.getFirstName(), request.getLastName(), request.getPhoneNumber());
    }

    private boolean checkAvailable(Client client) {
        return !repository.existsByFirstNameAndLastNameAndPhoneNumber(
                client.getFirstName(), client.getLastName(), client.getPhoneNumber());
    }
}
