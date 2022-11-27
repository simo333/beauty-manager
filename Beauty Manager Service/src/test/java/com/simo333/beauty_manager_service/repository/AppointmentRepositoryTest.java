package com.simo333.beauty_manager_service.repository;

import com.simo333.beauty_manager_service.model.Appointment;
import com.simo333.beauty_manager_service.model.Client;
import com.simo333.beauty_manager_service.model.Treatment;
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
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private TreatmentRepository treatmentRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Test
    void shouldReturnAppointmentsByClientId() {
        // given
        Client client = new Client();
        client.setFirstName("name");
        client.setLastName("lastName");
        client.setPhoneNumber("+11123456789");

        Treatment treatment = new Treatment();
        treatment.setDuration(Duration.of(30, ChronoUnit.MINUTES));
        treatment.setName("Treatment");
        treatment.setPrice(BigDecimal.valueOf(30));
        treatment.setDescription("description");

        Appointment appointment = new Appointment();
        appointment.setDateTime(ZonedDateTime.now().plus(1, ChronoUnit.HOURS));
        appointment.setClient(client);
        appointment.setTreatment(treatment);

        // when
        clientRepository.save(client);
        treatmentRepository.save(treatment);
        appointmentRepository.save(appointment);
        Page<Appointment> appointments = appointmentRepository
                .findAllByClientId(client.getId(), PageRequest.of(0, 20));

        // then
        assertThat(appointments)
                .hasSize(1)
                .contains(appointment);

    }

    @Test
    void shouldReturnAppointmentsByClientIdAndDate() {
        // given
        Client client = new Client();
        client.setFirstName("name");
        client.setLastName("lastName");
        client.setPhoneNumber("+11123456788");

        Treatment treatment = new Treatment();
        treatment.setDuration(Duration.of(30, ChronoUnit.MINUTES));
        treatment.setName("Treatment2");
        treatment.setPrice(BigDecimal.valueOf(30));
        treatment.setDescription("description");

        Appointment appointment = new Appointment();
        appointment.setDateTime(ZonedDateTime.now().plus(2, ChronoUnit.DAYS));
        appointment.setClient(client);
        appointment.setTreatment(treatment);

        // when
        clientRepository.save(client);
        treatmentRepository.save(treatment);
        appointmentRepository.save(appointment);
        Page<Appointment> appointments = appointmentRepository
                .findAllByClientIdAndDateTimeBetween(client.getId(),
                        ZonedDateTime.now().plus(1, ChronoUnit.DAYS),
                        ZonedDateTime.now().plus(3, ChronoUnit.DAYS),
                        PageRequest.of(0, 20));

        // then
        assertThat(appointments)
                .hasSize(1)
                .contains(appointment);

    }

    @Test
    void shouldReturnAppointmentsByAndDate() {
        // given
        Client client = new Client();
        client.setFirstName("name");
        client.setLastName("lastName");
        client.setPhoneNumber("+11123456785");

        Treatment treatment = new Treatment();
        treatment.setDuration(Duration.of(30, ChronoUnit.MINUTES));
        treatment.setName("Treatment3");
        treatment.setPrice(BigDecimal.valueOf(30));
        treatment.setDescription("description");

        Appointment appointment = new Appointment();
        appointment.setDateTime(ZonedDateTime.now().plus(7, ChronoUnit.DAYS));
        appointment.setClient(client);
        appointment.setTreatment(treatment);

        // when
        clientRepository.save(client);
        treatmentRepository.save(treatment);
        appointmentRepository.save(appointment);
        Page<Appointment> appointments = appointmentRepository
                .findAllByDateTimeBetween(ZonedDateTime.now().plus(6, ChronoUnit.DAYS),
                        ZonedDateTime.now().plus(8, ChronoUnit.DAYS), PageRequest.of(0, 20));

        // then
        assertThat(appointments)
                .hasSize(1)
                .contains(appointment);

    }

    @Test
    void shouldReturnTrueByDateBetween() {
        // given
        Client client = new Client();
        client.setFirstName("name");
        client.setLastName("lastName");
        client.setPhoneNumber("+11123456785");

        Treatment treatment = new Treatment();
        treatment.setDuration(Duration.of(30, ChronoUnit.MINUTES));
        treatment.setName("Treatment4");
        treatment.setPrice(BigDecimal.valueOf(30));
        treatment.setDescription("description");

        Appointment appointment = new Appointment();
        appointment.setDateTime(ZonedDateTime.now().plus(5, ChronoUnit.DAYS));
        appointment.setClient(client);
        appointment.setTreatment(treatment);

        // when
        clientRepository.save(client);
        treatmentRepository.save(treatment);
        appointmentRepository.save(appointment);
        boolean exists = appointmentRepository
                .existsByDateTimeBetween(ZonedDateTime.now().plus(4, ChronoUnit.DAYS),
                        ZonedDateTime.now().plus(6, ChronoUnit.DAYS));

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseByDateBetween() {
        // given
        ZonedDateTime badSinceDate = ZonedDateTime.now().minus(4, ChronoUnit.DAYS);
        ZonedDateTime badToDate = ZonedDateTime.now().minus(5, ChronoUnit.DAYS);

        // when
        boolean exists = appointmentRepository.existsByDateTimeBetween(badSinceDate, badToDate);

        // then
        assertThat(exists).isFalse();
    }

}