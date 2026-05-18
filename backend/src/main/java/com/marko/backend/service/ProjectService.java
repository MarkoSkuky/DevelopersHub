package com.marko.backend.service;

import com.marko.backend.dto.CreateProjectRequest;
import com.marko.backend.dto.ProjectResponse;
import com.marko.backend.exception.NotExistingProjectException;
import com.marko.backend.model.Project;
import com.marko.backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectResponse getProjectById(Long id) {
        Project project = projectRepository.getById(id)
            .orElseThrow(() -> new NotExistingProjectException(id));

        return new ProjectResponse(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getRequiredSkills(),
            project.isActive()
        );
    }

    public List<ProjectResponse> getAllProjects() {
        List<Project> projects = projectRepository.getAll();

        return projects.stream().map(p -> new ProjectResponse(
            p.getId(),
            p.getName(),
            p.getDescription(),
            p.getRequiredSkills(),
            p.isActive()
        )).toList();
    }

    public void removeProjectById(Long id) {
        projectRepository.getById(id)
            .orElseThrow(() -> new NotExistingProjectException(id));

        projectRepository.deleteById(id);
    }

    public ProjectResponse createNewProject(CreateProjectRequest request) {
        Project project = new Project();

        project.setName(request.name());
        project.setDescription(request.description());
        project.setRequiredSkills(request.requiredSkills());
        project.setActive(request.active());

        Project savedProject = projectRepository.save(project);

        return new ProjectResponse(
            savedProject.getId(),
            savedProject.getName(),
            savedProject.getDescription(),
            savedProject.getRequiredSkills(),
            savedProject.isActive()
        );
    }
}