package com.marko.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateProjectRequest(
    @NotBlank
    String name,

    @NotBlank
    String description,

    @NotEmpty
    List<String> requiredSkills,

    boolean active
) {
}
