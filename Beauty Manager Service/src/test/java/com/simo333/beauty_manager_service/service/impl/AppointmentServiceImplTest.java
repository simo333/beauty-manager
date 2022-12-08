package com.simo333.beauty_manager_service.service.impl;

import com.simo333.beauty_manager_service.model.Appointment;
import com.simo333.beauty_manager_service.model.Client;
import com.simo333.beauty_manager_service.model.Treatment;
import com.simo333.beauty_manager_service.repository.AppointmentRepository;
import com.simo333.beauty_manager_service.repository.ClientRepository;
import com.simo333.beauty_manager_service.repository.TreatmentRepository;
import com.simo333.beauty_manager_service.service.AppointmentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class AppointmentServiceImplTest {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private TreatmentRepository treatmentRepository;
    @Autowired
    private ClientRepository clientRepository;

    @AfterEach
    void cleanUp() {
        appointmentRepository.deleteAll();
        treatmentRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    void shouldReturnAppointments() {
        // given
        Treatment treatment = Treatment.builder()
                .name("treatment")
                .price(BigDecimal.TEN)
                .description("description")
                .duration(Duration.of(15, ChronoUnit.MINUTES))
                .build();

        Client client = Client.builder()
                .phoneNumber("+11123456789")
                .firstName("name")
                .lastName("lastName")
                .build();

        Appointment appointment = new Appointment();
        appointment.setTreatment(treatment);
        appointment.setClient(client);
        appointment.setDateTime(ZonedDateTime.now().plus(1, ChronoUnit.DAYS));

        // when
        treatmentRepository.save(treatment);
        clientRepository.save(client);
        appointmentRepository.save(appointment);
        Page<Appointment> actualAppointments = appointmentService.getAppointmentsPage(PageRequest.of(0, 20));

        // then
        assertThat(actualAppointments)
                .hasSize(1)
                .contains(appointment);
    }

    @Test
    void shouldReturnAppointmentsByClientId() {
        // given
        Treatment treatment = Treatment.builder()
                .name("treatment")
                .price(BigDecimal.TEN)
                .description("description")
                .duration(Duration.of(15, ChronoUnit.MINUTES))
                .build();

        Client client = Client.builder()
                .phoneNumber("+11123456789")
                .firstName("name")
                .lastName("lastName")
                .build();

        Appointment appointment = new Appointment();
        appointment.setTreatment(treatment);
        appointment.setClient(client);
        appointment.setDateTime(ZonedDateTime.now().plus(1, ChronoUnit.DAYS));

        // when
        treatmentRepository.save(treatment);
        clientRepository.save(client);
        appointmentRepository.save(appointment);
        Page<Appointment> actualAppointments = appointmentService
                .getAppointmentsByClient(client.getId(), PageRequest.of(0, 20));

        // then
        assertThat(actualAppointments)
                .hasSize(1)
                .contains(appointment);

    }

    @Test
    void shouldReturnAppointmentsByClientAndDate() {
        // given
        Treatment treatment = Treatment.builder()
                .name("treatment")
                .price(BigDecimal.TEN)
                .description("description")
                .duration(Duration.of(15, ChronoUnit.MINUTES))
                .build();

        Client client = Client.builder()
                .phoneNumber("+11123456789")
                .firstName("name")
                .lastName("lastName")
                .build();

        Appointment appointment = new Appointment();
        appointment.setTreatment(treatment);
        appointment.setClient(client);
        ZonedDateTime appointmentTime = ZonedDateTime.now().plus(1, ChronoUnit.DAYS);
        appointment.setDateTime(appointmentTime);

        // when
        treatmentRepository.save(treatment);
        clientRepository.save(client);
        appointmentRepository.save(appointment);
        Page<Appointment> actualAppointments = appointmentService.getAppointmentsByClientAndDate(
                client.getId(),
                appointmentTime.minus(1, ChronoUnit.HOURS),
                appointmentTime.plus(1, ChronoUnit.HOURS),
                PageRequest.of(0, 20));

        // then
        assertThat(actualAppointments)
                .hasSize(1)
                .contains(appointment);

    }

    @Test
    void shouldReturnAppointmentsByDate() {
        // given
        Treatment treatment = Treatment.builder()
                .name("treatment")
                .price(BigDecimal.TEN)
                .description("description")
                .duration(Duration.of(15, ChronoUnit.MINUTES))
                .build();

        Client client = Client.builder()
                .phoneNumber("+11123456789")
                .firstName("name")
                .lastName("lastName")
                .build();

        Appointment appointment = new Appointment();
        appointment.setTreatment(treatment);
        appointment.setClient(client);
        ZonedDateTime appointmentTime = ZonedDateTime.now().plus(1, ChronoUnit.DAYS);
        appointment.setDateTime(appointmentTime);

        // when
        treatmentRepository.save(treatment);
        clientRepository.save(client);
        appointmentRepository.save(appointment);
        Page<Appointment> actualAppointments = appointmentService.getAppointmentsByDate(
                appointmentTime.minus(1, ChronoUnit.HOURS),
                appointmentTime.plus(1, ChronoUnit.HOURS),
                PageRequest.of(0, 20));

        // then
        assertThat(actualAppointments)
                .hasSize(1)
                .contains(appointment);

    }

    @Test
    void shouldSaveAppointment() {
        // given
        Treatment treatment = Treatment.builder()
                .name("treatment")
                .price(BigDecimal.TEN)
                .description("description")
                .duration(Duration.of(15, ChronoUnit.MINUTES))
                .build();

        Client client = Client.builder()
                .phoneNumber("+11123456789")
                .firstName("name")
                .lastName("lastName")
                .build();

        Appointment appointment = new Appointment();
        appointment.setTreatment(treatment);
        appointment.setClient(client);
        ZonedDateTime appointmentTime = ZonedDateTime.now().plus(1, ChronoUnit.DAYS);
        appointment.setDateTime(appointmentTime);

        // when
        treatmentRepository.save(treatment);
        clientRepository.save(client);
        Appointment actualAppointment = appointmentService.save(appointment);

        // then
        assertThat(actualAppointment).isEqualTo(appointment);

    }

    @Test
    void shouldReturnAppointment() {
        // given
        Treatment treatment = Treatment.builder()
                .name("treatment")
                .price(BigDecimal.TEN)
                .description("description")
                .duration(Duration.of(15, ChronoUnit.MINUTES))
                .build();

        Client client = Client.builder()
                .phoneNumber("+11123456789")
                .firstName("name")
                .lastName("lastName")
                .build();

        Appointment appointment = new Appointment();
        appointment.setTreatment(treatment);
        appointment.setClient(client);
        ZonedDateTime appointmentTime = ZonedDateTime.now().plus(1, ChronoUnit.DAYS);
        appointment.setDateTime(appointmentTime);

        // when
        treatmentRepository.save(treatment);
        clientRepository.save(client);
        appointmentRepository.save(appointment);
        Appointment actualAppointment = appointmentService.getOne(appointment.getId());

        // then
        assertThat(actualAppointment).isEqualTo(appointment);
    }


    @Test
    void shouldDeleteAppointmentById() {
        // given
        Treatment treatment = Treatment.builder()
                .name("treatment")
                .price(BigDecimal.TEN)
                .description("description")
                .duration(Duration.of(15, ChronoUnit.MINUTES))
                .build();

        Client client = Client.builder()
                .phoneNumber("+11123456789")
                .firstName("name")
                .lastName("lastName")
                .build();

        Appointment appointment = new Appointment();
        appointment.setTreatment(treatment);
        appointment.setClient(client);
        ZonedDateTime appointmentTime = ZonedDateTime.now().plus(1, ChronoUnit.DAYS);
        appointment.setDateTime(appointmentTime);

        // when
        treatmentRepository.save(treatment);
        clientRepository.save(client);
        appointmentRepository.save(appointment);
        appointmentService.deleteById(appointment.getId());

        // then
        assertThat(appointmentRepository.findById(appointment.getId())).isEmpty();

    }
}