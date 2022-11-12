package com.simo333.beauty_manager_service.security.payload.treatment;

import com.simo333.beauty_manager_service.model.TreatmentCategory;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Duration;

@Getter
@Setter
public class TreatmentRequest {
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
    @NotNull
    @Valid
    private TreatmentCategory category;
}
