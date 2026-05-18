package com.marko.backend.controller;

import com.marko.backend.dto.CreateDeveloperRequest;
import com.marko.backend.dto.DeveloperResponse;
import com.marko.backend.service.DeveloperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/developers")
@RequiredArgsConstructor
public class DeveloperController {
    private final DeveloperService developerService;

    @GetMapping("/{id}")
    public ResponseEntity<DeveloperResponse> getDeveloperById(@PathVariable Long id) {
        return ResponseEntity.ok(developerService.getDeveloperById(id));
    }

    @GetMapping
    public ResponseEntity<List<DeveloperResponse>> getAllDevelopers() {
        return ResponseEntity.ok(developerService.getAllDevelopers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeDeveloperById(@PathVariable Long id) {
        developerService.removeDeveloperById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<DeveloperResponse> createNewDeveloper(@Valid @RequestBody CreateDeveloperRequest request) {
        DeveloperResponse response = developerService.createDeveloper(request);
        return ResponseEntity.status(201).body(response);
    }
}
