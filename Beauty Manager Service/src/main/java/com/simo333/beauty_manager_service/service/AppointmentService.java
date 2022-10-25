package com.simo333.beauty_manager_service.service;


import com.simo333.beauty_manager_service.dto.FreeBusyResponse;
import com.simo333.beauty_manager_service.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;

public interface AppointmentService {
    Page<Appointment> getAppointmentsPage(Pageable page);

    Page<Appointment> getAppointmentsByClient(Long clientId, Pageable page);

    Page<Appointment> getAppointmentsByClientAndDate(Long clientId, ZonedDateTime since, ZonedDateTime to, Pageable page);

    Page<Appointment> getAppointmentsByDate(ZonedDateTime since, ZonedDateTime to, Pageable page);

    Appointment save(Appointment appointment);

    Appointment getOne(Long id);

    Appointment update(Appointment appointment);

    void deleteById(Long id);

    FreeBusyResponse checkFreeBusy(Appointment appointment);
}
