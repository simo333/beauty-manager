package com.simo333.beauty_manager_service.security.payload.treatment_category;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class TreatmentCategoryRequest {
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;
}
