package com.simo333.beauty_manager_service.repository;

import com.simo333.beauty_manager_service.model.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    Page<Visit> findAllByClientId(Long clientId, Pageable page);

    Page<Visit> findAllByClientIdAndDateTimeBetween(Long clientId, LocalDateTime since, LocalDateTime to, Pageable page);

    Page<Visit> findAllByDateTimeBetween(LocalDateTime since, LocalDateTime to, Pageable page);

    boolean existsByDateTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Visit> findAllByDateTimeBetween(LocalDateTime start, LocalDateTime end);

}