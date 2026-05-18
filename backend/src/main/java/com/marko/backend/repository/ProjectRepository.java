package com.marko.backend.repository;

import com.marko.backend.model.Project;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProjectRepository {

    private final Map<Long, Project> projects = new HashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    public Project save(Project project) {
        Long id = idGenerator.getAndIncrement();
        project.setId(id);
        projects.put(id, project);
        return project;
    }

    public void deleteById(Long id) {
        projects.remove(id);
    }

    public Optional<Project> getById(Long id) {
        return Optional.ofNullable(projects.get(id));
    }

    public List<Project> getAll() {
        return new ArrayList<>(projects.values());
    }

}
