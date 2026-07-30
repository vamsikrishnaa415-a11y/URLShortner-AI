package com.aiurlshortener.analytics.repository;

import com.aiurlshortener.analytics.entity.AnalyticsEventEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEventEntity, Long> {

    List<AnalyticsEventEntity> findByShortCodeOrderByTimestampDesc(String shortCode);
}