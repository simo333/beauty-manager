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
    public Client save(Client client) {
        return repository.findByFirstNameAndLastNameAndPhoneNumber(
                        client.getFirstName(), client.getLastName(), client.getPhoneNumber())
                .orElseGet(() -> {
                    log.info("SAVE: Client with given parameters has been found in database. For: {}", client);
                    return repository.save(client);
                });
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
    public Client update(Client client) {
        getOne(client.getId());
        log.info("Updating client with id '{}'", client.getId());
        return repository.save(client);
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
}
