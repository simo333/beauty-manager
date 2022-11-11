package com.simo333.beauty_manager_service.service.impl;

import com.simo333.beauty_manager_service.security.payload.freebusy.FreeBusyResponse;
import com.simo333.beauty_manager_service.exception.FreeBusyException;
import com.simo333.beauty_manager_service.model.Appointment;
import com.simo333.beauty_manager_service.repository.AppointmentRepository;
import com.simo333.beauty_manager_service.service.AppointmentService;
import com.simo333.beauty_manager_service.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;
    private final ClientService clientService;

    @Override
    public Page<Appointment> getAppointmentsPage(Pageable page) {
        log.info("Fetching Appointments. {}", page);
        return repository.findAll(page);
    }

    @Override
    public Page<Appointment> getAppointmentsByClient(Long clientId, Pageable page) {
        log.info("Fetching Appointments for client with id '{}'. {}", clientId, page);
        return repository.findAllByClientId(clientId, page);
    }

    @Override
    public Page<Appointment> getAppointmentsByClientAndDate(Long clientId, ZonedDateTime since, ZonedDateTime to, Pageable page) {
        log.info("Fetching Appointments for client with id '{}' and date between {} and {}. {}",
                clientId, since, to, page);
        return repository.findAllByClientIdAndDateTimeBetween(clientId, since, to, page);
    }

    @Override
    public Page<Appointment> getAppointmentsByDate(ZonedDateTime since, ZonedDateTime to, Pageable page) {
        log.info("Fetching Appointments with date between {} and {}. {}",
                since, to, page);
        return repository.findAllByDateTimeBetween(since, to, page);
    }

    @Transactional
    @Override
    public Appointment save(Appointment appointment) {
        if (appointment.getClient().getId() == null) {
            if (clientService.exists(appointment.getClient())) {
                appointment.setClient(clientService.getOne(appointment.getClient()));
            } else {
                appointment.setClient(clientService.save(appointment.getClient()));
            }
        }
        checkFreeBusy(appointment);
        log.info("Saving a new appointment with: {}", appointment);
        return repository.save(appointment);
    }

    @Override
    public Appointment getOne(Long id) {
        Appointment appointment = repository.findById(id).orElseThrow(() -> {
            log.error("Appointment with id '{}' not found", id);
            throw new ResourceNotFoundException("Appointment not found. For id " + id);
        });
        log.info("Appointment with id '{}' has been found.", appointment.getId());
        return appointment;
    }

    @Transactional
    @Override
    public Appointment update(Appointment appointment) {
        getOne(appointment.getId());
        log.info("Updating appointment with id '{}'", appointment.getId());
        return repository.save(appointment);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        log.info("Deleting appointment with id '{}'", id);
        repository.deleteById(id);
    }

    public FreeBusyResponse checkFreeBusy(Appointment appointment) {
        if (isBusy(appointment)) {
            throw new FreeBusyException(getNextFreeTime(appointment));
        }
        return new FreeBusyResponse(isBusy(appointment), appointment.getDateTime().toString());
    }

    public boolean isBusy(Appointment appointment) {
        ZonedDateTime treatmentFinishTime = appointment.getDateTime().plus(appointment.getTreatment().getDuration());
        List<Appointment> allByDateTimeBetween = repository.findAllByDateTimeBetween(appointment.getDateTime(), treatmentFinishTime);

        getNextFreeTime(appointment);

        log.info("Check if busy: '{}' to '{}'.", appointment.getDateTime(), treatmentFinishTime);
        log.info("Found appointments: {}", allByDateTimeBetween.toString());
        return repository.existsByDateTimeBetween(appointment.getDateTime(), treatmentFinishTime);
    }

    public String getNextFreeTime(Appointment appointment) {
        ZonedDateTime treatmentFinishTime = appointment.getDateTime().plus(appointment.getTreatment().getDuration());
        List<Appointment> allByDateTimeBetween = repository.findAllByDateTimeBetween(appointment.getDateTime(), treatmentFinishTime);
        Optional<Appointment> lastDate = allByDateTimeBetween.stream().max(Comparator.comparing(Appointment::getFinishDateTime));
        if (lastDate.isPresent()) {
            ZonedDateTime finishDateTime = lastDate.get().getFinishDateTime();
            if (finishDateTime.toLocalTime().isAfter(LocalTime.of(16, 30))) {
                return null;
            }
            return finishDateTime.toOffsetDateTime().toString();
        }
        return appointment.getDateTime().toOffsetDateTime().toString();
    }
}
