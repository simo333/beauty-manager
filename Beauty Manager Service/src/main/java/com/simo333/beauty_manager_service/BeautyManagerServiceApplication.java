package com.simo333.beauty_manager_service;

import com.simo333.beauty_manager_service.model.AppUser;
import com.simo333.beauty_manager_service.model.Client;
import com.simo333.beauty_manager_service.model.Role;
import com.simo333.beauty_manager_service.service.ClientService;
import com.simo333.beauty_manager_service.service.RoleService;
import com.simo333.beauty_manager_service.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;

@SpringBootApplication
public class BeautyManagerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeautyManagerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService, RoleService roleService, ClientService clientService) {
        return args -> {
            roleService.save(new Role(null, Role.Type.ROLE_USER));
            roleService.save(new Role(null, Role.Type.ROLE_ADMIN));

            Client client1 = clientService.save(new Client(null, "John", "Doe", "+48123456789"));
            Client client2 = clientService.save(new Client(null, "Jim", "Carrey", "+49123456789"));
            Client client3 = clientService.save(new Client(null, "Arnold", "Schwarzenegger", "+45123456789"));

            userService.save(new AppUser(null, "john@mail.com", "12345678", new HashSet<>(), client1));
            userService.save(new AppUser(null, "jim@mail.com", "12345678", new HashSet<>(), client2));
            userService.save(new AppUser(null, "arnold@mail.com", "12345678", new HashSet<>(), client3));

            userService.addRoleToUser(1L, 1L);
            userService.addRoleToUser(2L, 2L);
            userService.addRoleToUser(3L, 2L);
            userService.addRoleToUser(3L, 1L);
        };
    }

}
