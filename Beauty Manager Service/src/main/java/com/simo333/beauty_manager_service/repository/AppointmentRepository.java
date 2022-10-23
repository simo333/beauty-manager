package com.simo333.beauty_manager_service.repository;

import com.simo333.beauty_manager_service.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findAllByClientId(Long clientId, Pageable page);

    Page<Appointment> findAllByClientIdAndDateTimeBetween(Long clientId, ZonedDateTime since, ZonedDateTime to, Pageable page);

    Page<Appointment> findAllByDateTimeBetween(ZonedDateTime since, ZonedDateTime to, Pageable page);

    boolean existsByDateTimeBetween(ZonedDateTime start, ZonedDateTime end);

    List<Appointment> findAllByDateTimeBetween(ZonedDateTime start, ZonedDateTime end);

}