package com.aiurlshortener.url.service;

import com.aiurlshortener.url.dto.CreateShortUrlRequest;
import com.aiurlshortener.url.dto.CreateShortUrlResponse;
import com.aiurlshortener.url.entity.UrlEntity;
import com.aiurlshortener.url.event.UrlRedirectedEvent;
import com.aiurlshortener.url.exception.ShortUrlNotFoundException;
import com.aiurlshortener.url.mapper.UrlMapper;
import com.aiurlshortener.url.repository.UrlRepository;
import com.aiurlshortener.url.util.RandomBase62Generator;
import java.time.Instant;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UrlServiceImpl implements UrlService {

    private static final int MAX_SHORT_CODE_ATTEMPTS = 5;
    private static final int SHORT_CODE_LENGTH = 8;

    private final UrlRepository urlRepository;
    private final UrlMapper urlMapper;
    private final RandomBase62Generator randomBase62Generator;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UrlServiceImpl(
            UrlRepository urlRepository,
            UrlMapper urlMapper,
            RandomBase62Generator randomBase62Generator,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.urlRepository = urlRepository;
        this.urlMapper = urlMapper;
        this.randomBase62Generator = randomBase62Generator;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public CreateShortUrlResponse createShortUrl(CreateShortUrlRequest request) {
        String originalUrl = request.originalUrl().trim();
        return urlRepository.findByOriginalUrl(originalUrl)
                .map(existingUrl -> urlMapper.toCreateShortUrlResponse(existingUrl, false))
                .orElseGet(() -> createNewShortUrl(originalUrl));
    }

    @Override
    @Transactional
    public String resolveOriginalUrl(String shortCode) {
        UrlEntity urlEntity = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ShortUrlNotFoundException(shortCode));
        urlEntity.incrementClickCount();
        applicationEventPublisher.publishEvent(new UrlRedirectedEvent(shortCode, Instant.now()));
        return urlEntity.getOriginalUrl();
    }

    private CreateShortUrlResponse createNewShortUrl(String originalUrl) {
        for (int attempt = 0; attempt < MAX_SHORT_CODE_ATTEMPTS; attempt++) {
            String shortCode = generateShortCode();
            if (!urlRepository.existsByShortCode(shortCode)) {
                UrlEntity savedUrl = urlRepository.save(
                        new UrlEntity(shortCode, originalUrl, Instant.now())
                );
                return urlMapper.toCreateShortUrlResponse(savedUrl, true);
            }
        }

        throw new IllegalStateException("Unable to generate a unique short code");
    }

    private String generateShortCode() {
        return randomBase62Generator.generate(SHORT_CODE_LENGTH);
    }
}