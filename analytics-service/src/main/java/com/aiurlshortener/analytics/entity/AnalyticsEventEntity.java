package com.aiurlshortener.analytics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "analytics_events")
public class AnalyticsEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String shortCode;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false, length = 45)
    private String ipAddress;

    @Column(nullable = false, length = 512)
    private String browser;

    protected AnalyticsEventEntity() {
    }

    public AnalyticsEventEntity(
            String shortCode,
            String originalUrl,
            Instant timestamp,
            String ipAddress,
            String browser
    ) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
        this.browser = browser;
    }

    public Long getId() {
        return id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getBrowser() {
        return browser;
    }
}