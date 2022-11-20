package com.simo333.beauty_manager_service.controller;

import com.simo333.beauty_manager_service.model.Client;
import com.simo333.beauty_manager_service.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ClientService clientService;

    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void shouldReturnAllClients() throws Exception {
        Long clientId = 1L;
        String name = "name";
        String lastName = "lastname";
        String phone = "+11123456789";
        Client client = new Client(clientId, name, lastName, phone);

        List<Client> clients = new ArrayList<>();
        clients.add(client);

        given(clientService.getClients()).willReturn(clients);

        mvc.perform(get("/api/clients/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(clients.size())));
    }

    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void shouldReturnOneClient() throws Exception {
        Long clientId = 1L;
        String name = "name";
        String lastName = "lastname";
        String phone = "+11123456789";
        Client client = new Client(clientId, name, lastName, phone);

        given(clientService.getOne(clientId)).willReturn(client);

        mvc.perform(get("/api/clients/{id}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(client.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(client.getLastName())))
                .andExpect(jsonPath("$.phoneNumber", is(client.getPhoneNumber())));
    }

    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void shouldReturnNotFound() throws Exception {
        Long clientId = 1L;

        given(clientService.getOne(clientId)).willThrow(ResourceNotFoundException.class);

        mvc.perform(get("/api/clients/{id}", clientId))
                .andExpect(status().isNotFound());
    }

}