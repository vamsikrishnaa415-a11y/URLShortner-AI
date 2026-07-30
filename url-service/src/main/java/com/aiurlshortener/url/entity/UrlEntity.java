package com.aiurlshortener.url.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "short_urls", indexes = {
    @Index(name = "idx_short_url_short_code", columnList = "shortCode"),
    @Index(name = "idx_short_url_original_url", columnList = "originalUrl")
})
/**
 * Persistent mapping between a generated short code and its destination URL.
 */
public class UrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String shortCode;

    @Column(nullable = false, unique = true, length = 2048)
    private String originalUrl;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private long clickCount;

    protected UrlEntity() {
    }

    /**
     * Creates a URL mapping with an initial click count of zero.
     *
     * @param shortCode generated Base62 identifier
     * @param originalUrl validated destination URL
     * @param createdAt mapping creation time
     */
    public UrlEntity(String shortCode, String originalUrl, Instant createdAt) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public long getClickCount() {
        return clickCount;
    }

    public void incrementClickCount() {
        clickCount++;
    }
}