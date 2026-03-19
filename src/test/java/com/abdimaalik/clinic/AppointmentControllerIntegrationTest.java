package com.abdimaalik.clinic;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class AppointmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String appointmentId;

    @Test
    @Order(1)
    void shouldScheduleAppointmentSuccessfully() throws Exception {
        String requestBody = """
                {
                  "patientName": "Ali Hassan",
                  "clinicianName": "Dr Brown",
                  "startTime": "2026-03-20T10:00:00",
                  "endTime": "2026-03-20T10:30:00",
                  "fee": 75.00
                }
                """;

        String response = mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientName").value("Ali Hassan"))
                .andExpect(jsonPath("$.clinicianName").value("Dr Brown"))
                .andExpect(jsonPath("$.fee").value(new BigDecimal("75.00").doubleValue()))
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        appointmentId = jsonNode.get("id").asText();
    }

    @Test
    @Order(2)
    void shouldRejectOverlappingAppointment() throws Exception {
        String requestBody = """
                {
                  "patientName": "Sara Khan",
                  "clinicianName": "Dr Brown",
                  "startTime": "2026-03-20T10:15:00",
                  "endTime": "2026-03-20T10:45:00",
                  "fee": 80.00
                }
                """;

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Appointment overlaps with an existing active appointment for this clinician."));
    }

    @Test
    @Order(3)
    void shouldCancelAppointmentSuccessfully() throws Exception {
        mockMvc.perform(patch("/appointments/" + appointmentId + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    @Order(4)
    void shouldAllowRebookingAfterCancellation() throws Exception {
        String requestBody = """
                {
                  "patientName": "Sara Khan",
                  "clinicianName": "Dr Brown",
                  "startTime": "2026-03-20T10:15:00",
                  "endTime": "2026-03-20T10:45:00",
                  "fee": 80.00
                }
                """;

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    @Order(5)
    void shouldReturnPaginatedAppointments() throws Exception {
        mockMvc.perform(get("/appointments?page=0&size=5&sortBy=startTime&direction=asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.number").value(0));
    }
}
