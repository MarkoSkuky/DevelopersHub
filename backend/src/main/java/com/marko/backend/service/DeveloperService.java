package com.marko.backend.service;

import com.marko.backend.dto.CreateDeveloperRequest;
import com.marko.backend.dto.DeveloperResponse;
import com.marko.backend.exception.DeveloperAlreadyExistException;
import com.marko.backend.exception.NotExistingDeveloper;
import com.marko.backend.model.Developer;
import com.marko.backend.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeveloperService {

    private final DeveloperRepository developerRepository;

    public DeveloperResponse getDeveloperById(Long id) {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(() -> new NotExistingDeveloper(id));

        return new DeveloperResponse(
                developer.getId(),
                developer.getName(),
                developer.getEmail(),
                developer.getSeniority(),
                developer.getSkills(),
                developer.getSalaryExpectation()
        );
    }


    public List<DeveloperResponse> getAllDevelopers() {
        List<Developer> developers = developerRepository.findAll();

        return developers.stream().map(d -> new DeveloperResponse(
                d.getId(),
                d.getName(),
                d.getEmail(),
                d.getSeniority(),
                d.getSkills(),
                d.getSalaryExpectation()
        )).toList();
    }

    public void removeDeveloperById(Long id) {
        developerRepository.findById(id)
                .orElseThrow(() -> new NotExistingDeveloper(id));
        developerRepository.deleteById(id);
    }

    public DeveloperResponse createDeveloper(CreateDeveloperRequest request) {
        if (developerRepository.existsByEmail(request.email())) {
            throw new DeveloperAlreadyExistException(request.email());
        }

        Developer developer = new Developer();
        developer.setName(request.name());
        developer.setEmail(request.email());
        developer.setSeniority(request.seniority());
        developer.setSalaryExpectation(request.salaryExpectation());
        developer.setSkills(request.skills());

        Developer savedDeveloper = developerRepository.save(developer);
        return new DeveloperResponse(
                savedDeveloper.getId(),
                savedDeveloper.getName(),
                savedDeveloper.getEmail(),
                savedDeveloper.getSeniority(),
                savedDeveloper.getSkills(),
                savedDeveloper.getSalaryExpectation());

    }
}
