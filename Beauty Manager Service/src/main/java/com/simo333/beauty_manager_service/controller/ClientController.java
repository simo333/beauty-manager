package com.simo333.beauty_manager_service.controller;

import com.simo333.beauty_manager_service.model.Client;
import com.simo333.beauty_manager_service.security.payload.client.ClientRequest;
import com.simo333.beauty_manager_service.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientService service;

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<Page<Client>> getClientsPage(Pageable page) {
        Page<Client> clients = service.getClientsPage(page);
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/all")
    public ResponseEntity<List<Client>> getClients() {
        List<Client> clients = service.getClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        Client client = service.getOne(id);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Client> saveClient(@RequestBody @Valid ClientRequest request) {
        Client saved = service.save(request);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody @Valid ClientRequest request) {
        Client updated = service.update(id, request);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteClient(@PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
