package com.aiurlshortener.analytics.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aiurlshortener.analytics.dto.AnalyticsEventResponse;
import com.aiurlshortener.analytics.dto.AnalyticsSummaryResponse;
import com.aiurlshortener.analytics.exception.AnalyticsExceptionHandler;
import com.aiurlshortener.analytics.service.AnalyticsService;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AnalyticsController.class)
@Import(AnalyticsExceptionHandler.class)
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

        @MockitoBean
    private AnalyticsService analyticsService;

    @Test
    void createsAnalyticsEvent() throws Exception {
        when(analyticsService.recordEvent(any())).thenReturn(new AnalyticsEventResponse(
                1L,
                "A1b2C3d4",
                "https://example.com/page",
                Instant.parse("2026-07-30T10:00:00Z"),
                "203.0.113.10",
                "Mozilla/5.0"
        ));

        mockMvc.perform(post("/analytics/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"shortCode":"A1b2C3d4","originalUrl":"https://example.com/page","timestamp":"2026-07-30T10:00:00Z","ipAddress":"203.0.113.10","browser":"Mozilla/5.0"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortCode").value("A1b2C3d4"));
    }

    @Test
    void retrievesAnalyticsSummary() throws Exception {
        when(analyticsService.getAnalytics("A1b2C3d4")).thenReturn(new AnalyticsSummaryResponse(
                "A1b2C3d4", 2, Instant.parse("2026-07-30T10:00:00Z")
        ));

        mockMvc.perform(get("/api/v1/analytics/A1b2C3d4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalClicks").value(2))
                .andExpect(jsonPath("$.latestRedirectAt").value("2026-07-30T10:00:00Z"));
    }

    @Test
    void rejectsInvalidShortCode() throws Exception {
        mockMvc.perform(get("/api/v1/analytics/invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}