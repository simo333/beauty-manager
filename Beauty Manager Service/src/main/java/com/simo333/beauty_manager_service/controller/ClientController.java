package com.simo333.beauty_manager_service.controller;

import com.simo333.beauty_manager_service.model.Client;
import com.simo333.beauty_manager_service.service.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientServiceImpl clientService;

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<Page<Client>> getClientsPage(Pageable page) {
        Page<Client> clients = clientService.getAllByPage(page);
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        Client client = clientService.getOne(id);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Client> saveClient(@RequestBody @Valid Client client) {
        Client saved = clientService.save(client);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody @Valid Client client) {
        client.setId(id);
        Client updated = clientService.save(client);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        clientService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
