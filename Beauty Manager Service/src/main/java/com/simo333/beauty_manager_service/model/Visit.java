package com.simo333.beauty_manager_service.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "visits")
@Data
@NoArgsConstructor
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @OneToOne
    private Treatment treatment;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;
    @NotNull
    @Future
    private LocalDateTime dateTime;
    private LocalDateTime bookedAt;

    @PrePersist
    public void prePersist() {
        bookedAt = LocalDateTime.now(ZoneId.of("Europe/Warsaw"));
    }

    public Visit(Long id, Treatment treatment, Client client, LocalDateTime dateTime) {
        this.id = id;
        this.treatment = treatment;
        this.client = client;
        this.dateTime = dateTime;
    }

    public LocalDateTime getFinishDateTime() {
        return dateTime.plus(treatment.getDuration());
    }
}
