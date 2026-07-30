package com.aiurlshortener.analytics.repository;

import com.aiurlshortener.analytics.entity.AnalyticsEventEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Persists analytics events and exposes aggregate lookup primitives.
 */
public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEventEntity, Long> {

    /**
     * Counts redirect events for a short code without loading event rows.
     *
     * @param shortCode the short code to aggregate
     * @return the total number of recorded events
     */
    long countByShortCode(String shortCode);

    /**
     * Retrieves only the latest event for a short code.
     *
     * @param shortCode the short code to inspect
     * @return the latest event when one exists
     */
    Optional<AnalyticsEventEntity> findFirstByShortCodeOrderByTimestampDesc(String shortCode);
} 