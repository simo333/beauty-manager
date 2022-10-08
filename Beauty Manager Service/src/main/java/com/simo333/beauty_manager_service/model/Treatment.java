package com.simo333.beauty_manager_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Duration;

@Entity
@Table(name = "treatments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Treatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;
    @NotBlank
    @Size(min = 2, max = 1000)
    private String description;
    @NotNull
    @Positive
    private BigDecimal price;
    @NotNull
    private Duration duration;
    @ManyToOne
    @JoinColumn(name = "treatment_category_id")
    private TreatmentCategory category;
}
