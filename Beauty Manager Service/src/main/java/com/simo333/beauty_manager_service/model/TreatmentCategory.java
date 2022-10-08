package com.simo333.beauty_manager_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "treatment_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;
}
