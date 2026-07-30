package com.aiurlshortener.url.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.aiurlshortener.url.entity.UrlEntity;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class UrlRepositoryTest {

    @Autowired
    private UrlRepository urlRepository;

    @Test
    void findsPersistedUrlByOriginalUrlAndShortCode() {
        UrlEntity savedUrl = urlRepository.save(new UrlEntity(
                "A1b2C3d4", "https://example.com/page", Instant.parse("2026-07-30T10:00:00Z")
        ));

        assertThat(urlRepository.findByOriginalUrl("https://example.com/page"))
                .containsSame(savedUrl);
        assertThat(urlRepository.findByShortCode("A1b2C3d4"))
                .containsSame(savedUrl);
        assertThat(urlRepository.existsByShortCode("A1b2C3d4")).isTrue();
    }
}