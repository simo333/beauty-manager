package com.simo333.beauty_manager_service;

import com.simo333.beauty_manager_service.model.*;
import com.simo333.beauty_manager_service.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.*;
import java.util.HashSet;

@SpringBootApplication
public class BeautyManagerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeautyManagerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService, RoleService roleService, ClientService clientService,
                          TreatmentCategoryService categoryService, TreatmentService treatmentService,
                          AppointmentService appointmentService) {
        return args -> {
            String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam consequat et mauris efficitur dictum. Aenean a finibus eros. Fusce venenatis venenatis magna, ac facilisis augue vestibulum nec. Interdum et malesuada fames ac ante ipsum primis in faucibus. Duis ut pretium neque, ut iaculis felis. Aenean eleifend risus ac aliquet consequat. Vestibulum scelerisque leo a enim malesuada vulputate. Praesent at nisi non ante consectetur placerat vel a urna. Integer tincidunt eros at leo dictum dictum in vel massa. Ut vel justo efficitur purus hendrerit dictum. Sed accumsan luctus maximus.";
            roleService.save(new Role(null, Role.Type.ROLE_USER));
            roleService.save(new Role(null, Role.Type.ROLE_ADMIN));

            Client client1 = clientService.save(new Client(null, "John", "Doe", "+48123456789"));
            Client client2 = clientService.save(new Client(null, "Jim", "Carrey", "+49123456789"));
            Client client3 = clientService.save(new Client(null, "Arnold", "Schwarzenegger", "+45123456789"));

            userService.save(new User(null, "john@mail.com", "12345678", new HashSet<>(), client1));
            userService.save(new User(null, "jim@mail.com", "12345678", new HashSet<>(), client2));
            userService.save(new User(null, "arnold@mail.com", "12345678", new HashSet<>(), client3));

            userService.addRoleToUser(1L, 1L);
            userService.addRoleToUser(2L, 2L);
            userService.addRoleToUser(3L, 2L);
            userService.addRoleToUser(3L, 1L);

            TreatmentCategory category1 = categoryService.save(new TreatmentCategory(null, "Zabiegi na twarz"));
            TreatmentCategory category2 = categoryService.save(new TreatmentCategory(null, "Zabiegi na dłonie"));
            TreatmentCategory category3 = categoryService.save(new TreatmentCategory(null, "Zabiegi na stopy"));

            Treatment treatment1 = treatmentService.save(new Treatment(null, "Mikrodermabrazja", description,
                    BigDecimal.valueOf(10), Duration.ofMinutes(60), category1));
            Treatment treatment2 = treatmentService.save(new Treatment(null, "Oksybrazja", description,
                    BigDecimal.valueOf(20), Duration.ofMinutes(30), category1));
            Treatment treatment3 = treatmentService.save(new Treatment(null, "Makijaż Pernamentny", description,
                    BigDecimal.valueOf(30), Duration.ofMinutes(15), category1));
            Treatment treatment4 = treatmentService.save(new Treatment(null, "Stylizacja Brwi i Rzęs", description,
                    BigDecimal.valueOf(40), Duration.ofMinutes(45), category1));
            Treatment treatment5 = treatmentService.save(new Treatment(null, "Manicure", description,
                    BigDecimal.valueOf(50), Duration.ofMinutes(30), category2));
            Treatment treatment6 = treatmentService.save(new Treatment(null, "Masaż dłoni", description,
                    BigDecimal.valueOf(50), Duration.ofMinutes(30), category2));
            Treatment treatment7 = treatmentService.save(new Treatment(null, "Pedicure", description,
                    BigDecimal.valueOf(80), Duration.ofMinutes(45), category3));

            ZoneOffset zone = ZoneOffset.ofHours(+2);
            ZonedDateTime now = ZonedDateTime.now(zone);

            appointmentService.save(new Appointment(null, treatment1, client1, now.plusDays(5)));
            appointmentService.save(new Appointment(null, treatment2, client1, now.plusDays(6)));
            appointmentService.save(new Appointment(null, treatment3, client1, now.plusDays(3)));
            appointmentService.save(new Appointment(null, treatment3, client1, now.plusDays(7)));
            appointmentService.save(new Appointment(null, treatment3, client1, now.plusDays(8)));
            appointmentService.save(new Appointment(null, treatment1, client1, ZonedDateTime.of(
                    LocalDate.now().plusDays(2), LocalTime.of(10, 0), zone)));
            appointmentService.save(new Appointment(null, treatment1, client1, ZonedDateTime.of(
                    LocalDate.now().plusDays(2), LocalTime.of(11, 40), zone)));
            appointmentService.save(new Appointment(null, treatment1, client1, ZonedDateTime.of(
                    LocalDate.now().plusDays(2), LocalTime.of(14, 0), zone)));
        };
    }

}
