package com.marko.backend.repository;

import com.marko.backend.model.Developer;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class DeveloperRepository {
    private final Map<Long, Developer> developers = new HashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    public Optional<Developer> getById(Long id) {
        return Optional.ofNullable(developers.get(id));
    }

    public void deleteById(Long id) {
        developers.remove(id);
    }

    public Developer save(Developer developer) {
        Long id = idGenerator.getAndIncrement();
        developer.setId(id);
        developers.put(id, developer);
        return developer;
    }

    public List<Developer> getAll() {
        return new ArrayList<>(developers.values());
    }
}
