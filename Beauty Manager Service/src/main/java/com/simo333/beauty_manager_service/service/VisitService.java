package com.simo333.beauty_manager_service.service;


import com.simo333.beauty_manager_service.model.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public interface VisitService {
    Page<Visit> getVisitsPage(Pageable page);

    Page<Visit> getVisitsByClient(Long clientId, Pageable page);

    Page<Visit> getVisitsByClientAndDate(Long clientId, ZonedDateTime since, ZonedDateTime to, Pageable page);

    Page<Visit> getVisitsByDate(ZonedDateTime since, ZonedDateTime to, Pageable page);

    Visit save(Visit visit);

    Visit getOne(Long id);

    Visit update(Visit visit);

    void deleteById(Long id);
}
