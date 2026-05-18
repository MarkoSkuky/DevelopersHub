package com.marko.backend.dto;

import java.util.List;

public record ProjectResponse(

    Long id,

    String name,

    String description,

    List<String> requiredSkills,

    boolean active

) {
}