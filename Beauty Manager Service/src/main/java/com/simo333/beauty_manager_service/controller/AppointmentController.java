package com.simo333.beauty_manager_service.controller;

import com.simo333.beauty_manager_service.dto.FreeBusyResponse;
import com.simo333.beauty_manager_service.model.Appointment;
import com.simo333.beauty_manager_service.service.AppointmentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;

@Controller
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {

    private final AppointmentServiceImpl service;

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<Page<Appointment>> getAppointments(Pageable page) {
        Page<Appointment> appointments = service.getAppointmentsPage(page);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/client/{id}")
    public ResponseEntity<Page<Appointment>> getAppointmentsByClient(Pageable page, @PathVariable Long id) {
        Page<Appointment> appointments = service.getAppointmentsByClient(id, page);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/client/{id}/date/{since}/{to}")
    public ResponseEntity<Page<Appointment>> getAppointmentsByClientAndDates(
            Pageable page,
            @PathVariable Long id,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime since,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime to) {
        Page<Appointment> appointments = service.getAppointmentsByClientAndDate(id, since, to, page);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/date/{since}/{to}")
    public ResponseEntity<Page<Appointment>> getAppointmentsByDates(
            Pageable page,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime since,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime to) {
        Page<Appointment> appointments = service.getAppointmentsByDate(since, to, page);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Appointment appointment = service.getOne(id);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Appointment> saveAppointment(@RequestBody @Valid Appointment appointment) {
        Appointment saved = service.save(appointment);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody @Valid Appointment appointment) {
        appointment.setId(id);
        Appointment updated = service.update(appointment);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/free-busy")
    public ResponseEntity<FreeBusyResponse> checkIfFree(@Valid @RequestBody Appointment appointment) {
        FreeBusyResponse response = service.checkFreeBusy(appointment);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
