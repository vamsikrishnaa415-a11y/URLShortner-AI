package com.aiurlshortener.analytics.controller;

import com.aiurlshortener.analytics.dto.AnalyticsEventRequest;
import com.aiurlshortener.analytics.dto.AnalyticsEventResponse;
import com.aiurlshortener.analytics.dto.AnalyticsSummaryResponse;
import com.aiurlshortener.analytics.service.AnalyticsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping("/analytics/events")
    public ResponseEntity<AnalyticsEventResponse> recordEvent(
            @Valid @RequestBody AnalyticsEventRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(analyticsService.recordEvent(request));
    }

    @GetMapping("/api/v1/analytics/{code}")
    public AnalyticsSummaryResponse getAnalytics(
            @PathVariable @Pattern(regexp = "^[0-9A-Za-z]{8}$", message = "code must be an 8-character Base62 value")
            String code
    ) {
        return analyticsService.getAnalytics(code);
    }
}