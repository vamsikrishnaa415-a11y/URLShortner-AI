package com.aiurlshortener.url.repository;

import com.aiurlshortener.url.entity.UrlEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<UrlEntity, Long> {

    boolean existsByShortCode(String shortCode);

    Optional<UrlEntity> findByOriginalUrl(String originalUrl);

    Optional<UrlEntity> findByShortCode(String shortCode);
}