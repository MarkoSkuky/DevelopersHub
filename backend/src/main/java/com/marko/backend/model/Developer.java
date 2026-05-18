package com.marko.backend.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Developer {

    private Long id;

    private String name;

    private String email;

    private Seniority seniority;

    private List<String> skills;

    private Integer salaryExpectation;

}
