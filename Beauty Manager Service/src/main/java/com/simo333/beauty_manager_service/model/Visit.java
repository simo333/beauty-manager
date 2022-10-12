package com.simo333.beauty_manager_service.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

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
    private ZonedDateTime dateTime;
    private ZonedDateTime bookedAt;

    @PrePersist
    public void prePersist() {
        bookedAt = ZonedDateTime.now(ZoneOffset.ofHours(+2));
    }

    public Visit(Long id, Treatment treatment, Client client, ZonedDateTime dateTime) {
        this.id = id;
        this.treatment = treatment;
        this.client = client;
        this.dateTime = dateTime;
    }

    public ZonedDateTime getFinishDateTime() {
        return dateTime.plus(treatment.getDuration());
    }
}
