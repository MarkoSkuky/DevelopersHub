package com.marko.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Project {
    private Long id;
    private String name;
    private String description;
    private List<String> requiredSkills;
    private boolean active;
}
