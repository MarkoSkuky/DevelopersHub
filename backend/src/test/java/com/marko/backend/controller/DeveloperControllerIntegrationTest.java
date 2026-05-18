package com.marko.backend.controller;

import com.marko.backend.dto.CreateDeveloperRequest;
import com.marko.backend.model.Seniority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DeveloperControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createDeveloper_validRequest_returnsCreatedDeveloper() throws Exception {
        CreateDeveloperRequest request = new CreateDeveloperRequest(
            "Marko Skukalek",
            "marko@gmail.com",
            Seniority.JUNIOR,
            List.of("java", "spring"),
            2100
        );

        mockMvc.perform(post("/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Marko Skukalek"))
            .andExpect(jsonPath("$.email").value("marko@gmail.com"))
            .andExpect(jsonPath("$.seniority").value("JUNIOR"))
            .andExpect(jsonPath("$.salaryExpectation").value(2100));
    }

    @Test
    void deleteDeveloper_existingId_returnsNoContent() throws Exception {
        CreateDeveloperRequest request = new CreateDeveloperRequest(
            "Marko Skukalek",
            "delete@test.com",
            Seniority.JUNIOR,
            List.of("java"),
            2100
        );

        String response = mockMvc.perform(post("/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/developers/" + id))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/developers/" + id))
            .andExpect(status().isNotFound());
    }

    @Test
    void createDeveloper_alreadyExistingEmail_throwsError400() throws Exception {
        CreateDeveloperRequest request1 = new CreateDeveloperRequest(
            "Marko Skukalek",
            "marko@gmail.com",
            Seniority.JUNIOR,
            List.of("java", "spring"),
            2100
        );

        CreateDeveloperRequest request2 = new CreateDeveloperRequest(
            "Ivan Samalek",
            "marko@gmail.com",
            Seniority.MEDIOR,
            List.of("java", "spring", "aws"),
            2600
        );

        mockMvc.perform(post("/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)));

        mockMvc.perform(post("/developers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request2)))
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.message")
                .value("Developer with email adress: " + request1.email() +  " already exist"));
    }

}
