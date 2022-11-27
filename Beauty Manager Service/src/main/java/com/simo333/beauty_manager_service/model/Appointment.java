package com.simo333.beauty_manager_service.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
public class Appointment {
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

    public Appointment(Long id, Treatment treatment, Client client, ZonedDateTime dateTime) {
        this.id = id;
        this.treatment = treatment;
        this.client = client;
        this.dateTime = dateTime;
    }

    public ZonedDateTime getFinishDateTime() {
        return dateTime.plus(treatment.getDuration());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
