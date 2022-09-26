package com.simo333.beauty_manager_service;

import com.simo333.beauty_manager_service.model.*;
import com.simo333.beauty_manager_service.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;

@SpringBootApplication
public class BeautyManagerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeautyManagerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService, RoleService roleService, ClientService clientService,
                          TreatmentCategoryService categoryService, TreatmentService treatmentService,
                          VisitService visitService) {
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

            TreatmentCategory category1 = categoryService.save(new TreatmentCategory(null, "Kategoria 1"));
            TreatmentCategory category2 = categoryService.save(new TreatmentCategory(null, "Kategoria 2"));
            TreatmentCategory category3 = categoryService.save(new TreatmentCategory(null, "Kategoria 3"));

            Treatment treatment1 = treatmentService.save(new Treatment(null, "Zabieg 1", "Opis",
                    BigDecimal.valueOf(10), Duration.ofMinutes(60), category1));
            Treatment treatment2 = treatmentService.save(new Treatment(null, "Zabieg 2", "Opis",
                    BigDecimal.valueOf(20), Duration.ofMinutes(30), category1));
            Treatment treatment3 = treatmentService.save(new Treatment(null, "Zabieg 3", "Opis",
                    BigDecimal.valueOf(30), Duration.ofMinutes(15), category1));
            Treatment treatment4 = treatmentService.save(new Treatment(null, "Zabieg 4", "Opis",
                    BigDecimal.valueOf(40), Duration.ofMinutes(45), category1));
            Treatment treatment5 = treatmentService.save(new Treatment(null, "Zabieg 5", "Opis",
                    BigDecimal.valueOf(50), Duration.ofMinutes(30), category2));

            visitService.save(new Visit(null, treatment1, client1, LocalDateTime.now().plusDays(5)));
            visitService.save(new Visit(null, treatment2, client1, LocalDateTime.now().plusDays(6)));
            visitService.save(new Visit(null, treatment3, client1, LocalDateTime.now().plusDays(3)));
            visitService.save(new Visit(null, treatment3, client1, LocalDateTime.now().plusDays(7)));
            visitService.save(new Visit(null, treatment3, client1, LocalDateTime.now().plusDays(8)));
        };
    }

}
