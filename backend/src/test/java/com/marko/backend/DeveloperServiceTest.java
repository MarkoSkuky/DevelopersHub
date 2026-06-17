package com.marko.backend;

import com.marko.backend.dto.CreateDeveloperRequest;
import com.marko.backend.dto.DeveloperResponse;
import com.marko.backend.exception.DeveloperAlreadyExistException;
import com.marko.backend.exception.NotExistingDeveloper;
import com.marko.backend.model.Developer;
import com.marko.backend.model.Seniority;
import com.marko.backend.repository.DeveloperRepository;
import com.marko.backend.service.DeveloperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeveloperServiceTest {

    private Developer developer;

    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private DeveloperService developerService;

    @BeforeEach
    void setUp() {
        developer = new Developer();
        developer.setId(1L);
        developer.setName("Marko Skukalek");
        developer.setEmail("marko@gmail.com");
        developer.setSeniority(Seniority.JUNIOR);
        developer.setSkills(List.of("java", "spring", "aws"));
        developer.setSalaryExpectation(2100);
    }

    @Test
    void getDeveloperById_existingId_returnsDeveloper() {
        when(developerRepository.findById(1L))
            .thenReturn(Optional.of(developer));

        DeveloperResponse response = developerService.getDeveloperById(1L);

        assertEquals(1L, response.id());
        assertEquals("Marko Skukalek", response.name());
        assertEquals("marko@gmail.com", response.email());
        assertEquals(Seniority.JUNIOR, response.seniority());
        assertEquals(List.of("java", "spring", "aws"), response.skills());
        assertEquals(2100, response.salaryExpectation());
    }

    @Test
    void getDeveloperById_nonExistingId_throwsNotExistingDeveloper() {
        when(developerRepository.findById(99L))
            .thenReturn(Optional.empty());

        assertThrows(
            NotExistingDeveloper.class,
            () -> developerService.getDeveloperById(99L)
        );
    }

    @Test
    void getAllDevelopers_existingDevelopers_returnsDeveloperResponses() {
        Developer secondDeveloper = new Developer();
        secondDeveloper.setId(2L);
        secondDeveloper.setName("Peter Novak");
        secondDeveloper.setEmail("peter@gmail.com");
        secondDeveloper.setSeniority(Seniority.SENIOR);
        secondDeveloper.setSkills(List.of("java", "docker"));
        secondDeveloper.setSalaryExpectation(3500);

        when(developerRepository.findAll())
            .thenReturn(List.of(developer, secondDeveloper));

        List<DeveloperResponse> responses = developerService.getAllDevelopers();

        assertEquals(2, responses.size());
        assertEquals("Marko Skukalek", responses.get(0).name());
        assertEquals("Peter Novak", responses.get(1).name());
        assertEquals(Seniority.SENIOR, responses.get(1).seniority());
    }

    @Test
    void getAllDevelopers_emptyRepository_returnsEmptyList() {
        when(developerRepository.findAll())
            .thenReturn(List.of());

        List<DeveloperResponse> responses = developerService.getAllDevelopers();

        assertTrue(responses.isEmpty());
    }

    @Test
    void removeDeveloperById_existingId_deletesDeveloper() {
        when(developerRepository.findById(1L))
            .thenReturn(Optional.of(developer));

        developerService.removeDeveloperById(1L);

        verify(developerRepository).deleteById(1L);
    }

    @Test
    void removeDeveloperById_nonExistingId_throwsNotExistingDeveloper() {
        when(developerRepository.findById(99L))
            .thenReturn(Optional.empty());

        assertThrows(
            NotExistingDeveloper.class,
            () -> developerService.removeDeveloperById(99L)
        );

        verify(developerRepository, never()).deleteById(99L);
    }

    @Test
    void createDeveloper_validRequest_createsDeveloper() {
        CreateDeveloperRequest request = new CreateDeveloperRequest(
            "Marko Skukalek",
            "marko@gmail.com",
            Seniority.JUNIOR,
            List.of("java", "spring", "aws"),
            2100
        );

        when(developerRepository.existsByEmail(request.email()))
            .thenReturn(false);

        when(developerRepository.save(any(Developer.class)))
            .thenReturn(developer);

        DeveloperResponse response = developerService.createDeveloper(request);

        assertEquals(1L, response.id());
        assertEquals("Marko Skukalek", response.name());
        assertEquals("marko@gmail.com", response.email());
        assertEquals(Seniority.JUNIOR, response.seniority());
        assertEquals(List.of("java", "spring", "aws"), response.skills());
        assertEquals(2100, response.salaryExpectation());

        verify(developerRepository).save(any(Developer.class));
    }

    @Test
    void createDeveloper_duplicateEmail_throwsDeveloperAlreadyExistException() {
        CreateDeveloperRequest request = new CreateDeveloperRequest(
            "Another Marko",
            "marko@gmail.com",
            Seniority.MEDIOR,
            List.of("react"),
            2500
        );

        when(developerRepository.existsByEmail(request.email()))
            .thenReturn(true);

        assertThrows(
            DeveloperAlreadyExistException.class,
            () -> developerService.createDeveloper(request)
        );

        verify(developerRepository, never()).save(any(Developer.class));
    }
}