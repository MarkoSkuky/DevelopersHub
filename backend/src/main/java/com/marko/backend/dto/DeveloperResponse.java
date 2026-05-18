package com.marko.backend.dto;

import com.marko.backend.model.Seniority;

import java.util.List;

public record DeveloperResponse(
    Long id,
    String name,
    String email,
    Seniority seniority,
    List<String> skills,
    Integer salaryExpectation
) {
}
