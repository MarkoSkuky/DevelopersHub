package com.marko.backend.dto;

import com.marko.backend.model.Seniority;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreateDeveloperRequest(
    @NotBlank
    String name,

    @Email
    String email,

    @NotNull
    Seniority seniority,

    @NotEmpty
    List<String> skills,

    @Positive
    Integer salaryExpectation
) {

}
