package com.aiurlshortener.analytics.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.aiurlshortener.analytics.entity.AnalyticsEventEntity;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class AnalyticsEventRepositoryTest {

    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    @Test
    void returnsEventsForShortCodeInDescendingTimestampOrder() {
        AnalyticsEventEntity olderEvent = analyticsEventRepository.save(new AnalyticsEventEntity(
                "A1b2C3d4", "https://example.com/page", Instant.parse("2026-07-30T10:00:00Z"),
                "203.0.113.10", "Mozilla/5.0"
        ));
        AnalyticsEventEntity newerEvent = analyticsEventRepository.save(new AnalyticsEventEntity(
                "A1b2C3d4", "https://example.com/page", Instant.parse("2026-07-30T11:00:00Z"),
                "203.0.113.10", "Mozilla/5.0"
        ));

        List<AnalyticsEventEntity> events = analyticsEventRepository.findByShortCodeOrderByTimestampDesc("A1b2C3d4");

        assertThat(events).containsExactly(newerEvent, olderEvent);
    }
}