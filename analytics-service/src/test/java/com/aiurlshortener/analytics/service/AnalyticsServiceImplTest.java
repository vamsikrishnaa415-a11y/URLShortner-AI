package com.aiurlshortener.analytics.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aiurlshortener.analytics.dto.AnalyticsEventRequest;
import com.aiurlshortener.analytics.dto.AnalyticsEventResponse;
import com.aiurlshortener.analytics.dto.AnalyticsSummaryResponse;
import com.aiurlshortener.analytics.entity.AnalyticsEventEntity;
import com.aiurlshortener.analytics.repository.AnalyticsEventRepository;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceImplTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    @Captor
    private ArgumentCaptor<AnalyticsEventEntity> eventCaptor;

    @Test
    void storesTheCompleteAnalyticsEvent() {
        Instant timestamp = Instant.parse("2026-07-30T10:15:30Z");
        AnalyticsEventRequest request = new AnalyticsEventRequest(
                "A1b2C3d4",
                "https://example.com/page",
                timestamp,
                "203.0.113.10",
                "Mozilla/5.0"
        );
        AnalyticsEventEntity savedEvent = new AnalyticsEventEntity(
                request.shortCode(),
                request.originalUrl(),
                request.timestamp(),
                request.ipAddress(),
                request.browser()
        );

        when(analyticsEventRepository.save(any(AnalyticsEventEntity.class))).thenReturn(savedEvent);

        AnalyticsEventResponse response = analyticsService.recordEvent(request);

        verify(analyticsEventRepository).save(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getShortCode()).isEqualTo(request.shortCode());
        assertThat(eventCaptor.getValue().getOriginalUrl()).isEqualTo(request.originalUrl());
        assertThat(eventCaptor.getValue().getTimestamp()).isEqualTo(request.timestamp());
        assertThat(eventCaptor.getValue().getIpAddress()).isEqualTo(request.ipAddress());
        assertThat(eventCaptor.getValue().getBrowser()).isEqualTo(request.browser());
        assertThat(response.shortCode()).isEqualTo(request.shortCode());
    }

    @Test
    void returnsClickCountAndLatestRedirectForAShortCode() {
        Instant latestTimestamp = Instant.parse("2026-07-30T11:00:00Z");
        AnalyticsEventEntity latestEvent = new AnalyticsEventEntity(
                "A1b2C3d4", "https://example.com/page", latestTimestamp, "203.0.113.10", "Mozilla/5.0"
        );
        AnalyticsEventEntity olderEvent = new AnalyticsEventEntity(
                "A1b2C3d4", "https://example.com/page", Instant.parse("2026-07-30T10:00:00Z"),
                "203.0.113.10", "Mozilla/5.0"
        );

        when(analyticsEventRepository.findByShortCodeOrderByTimestampDesc("A1b2C3d4"))
                .thenReturn(List.of(latestEvent, olderEvent));

        AnalyticsSummaryResponse response = analyticsService.getAnalytics("A1b2C3d4");

        assertThat(response.shortCode()).isEqualTo("A1b2C3d4");
        assertThat(response.totalClicks()).isEqualTo(2);
        assertThat(response.latestRedirectAt()).isEqualTo(latestTimestamp);
    }

    @Test
    void returnsZeroClicksWhenNoEventsHaveBeenStored() {
        when(analyticsEventRepository.findByShortCodeOrderByTimestampDesc("A1b2C3d4"))
                .thenReturn(List.of());

        AnalyticsSummaryResponse response = analyticsService.getAnalytics("A1b2C3d4");

        assertThat(response.totalClicks()).isZero();
        assertThat(response.latestRedirectAt()).isNull();
    }
}