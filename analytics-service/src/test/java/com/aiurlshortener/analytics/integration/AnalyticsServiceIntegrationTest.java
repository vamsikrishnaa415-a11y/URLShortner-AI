package com.aiurlshortener.analytics.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aiurlshortener.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AnalyticsServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    @BeforeEach
    void clearDatabase() {
        analyticsEventRepository.deleteAll();
    }

    @Test
    void storesEventAndReturnsAnalyticsSummary() throws Exception {
        mockMvc.perform(post("/analytics/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"shortCode":"A1b2C3d4","originalUrl":"https://example.com/page","timestamp":"2026-07-30T10:00:00Z","ipAddress":"203.0.113.10","browser":"Mozilla/5.0"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortCode").value("A1b2C3d4"));

        assertThat(analyticsEventRepository.countByShortCode("A1b2C3d4")).isEqualTo(1L);

        mockMvc.perform(get("/api/v1/analytics/A1b2C3d4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortCode").value("A1b2C3d4"))
                .andExpect(jsonPath("$.totalClicks").value(1))
                .andExpect(jsonPath("$.latestRedirectAt").value("2026-07-30T10:00:00Z"));
    }
}