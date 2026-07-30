package com.aiurlshortener.url.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.aiurlshortener.url.dto.CreateShortUrlRequest;
import com.aiurlshortener.url.dto.CreateShortUrlResponse;
import com.aiurlshortener.url.entity.UrlEntity;
import com.aiurlshortener.url.event.UrlRedirectedEvent;
import com.aiurlshortener.url.exception.DuplicateUrlException;
import com.aiurlshortener.url.exception.ShortUrlNotFoundException;
import com.aiurlshortener.url.mapper.UrlMapper;
import com.aiurlshortener.url.repository.UrlRepository;
import com.aiurlshortener.url.util.RandomBase62Generator;
import java.time.Instant;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class UrlServiceImplTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private UrlMapper urlMapper;

    @Mock
    private RandomBase62Generator randomBase62Generator;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private UrlServiceImpl urlService;

    @Captor
    private ArgumentCaptor<UrlEntity> urlEntityCaptor;

    @Test
    void createsAndReturnsANewShortUrl() {
        CreateShortUrlRequest request = new CreateShortUrlRequest("https://example.com/page");
        UrlEntity savedUrl = new UrlEntity("A1b2C3d4", request.originalUrl(), Instant.now());
        CreateShortUrlResponse expectedResponse = new CreateShortUrlResponse(
                "A1b2C3d4", request.originalUrl(), savedUrl.getCreatedAt(), true
        );

        when(urlRepository.findByOriginalUrl(request.originalUrl())).thenReturn(Optional.empty());
        when(randomBase62Generator.generate(8)).thenReturn("A1b2C3d4");
        when(urlRepository.existsByShortCode("A1b2C3d4")).thenReturn(false);
        when(urlRepository.save(any(UrlEntity.class))).thenReturn(savedUrl);
        when(urlMapper.toCreateShortUrlResponse(savedUrl, true)).thenReturn(expectedResponse);

        CreateShortUrlResponse response = urlService.createShortUrl(request);

        assertThat(response).isEqualTo(expectedResponse);
        verify(urlRepository).save(urlEntityCaptor.capture());
        assertThat(urlEntityCaptor.getValue().getShortCode()).isEqualTo("A1b2C3d4");
    }

    @Test
    void throwsConflictForADuplicateUrl() {
        CreateShortUrlRequest request = new CreateShortUrlRequest("https://example.com/page");
        UrlEntity existingUrl = new UrlEntity("A1b2C3d4", request.originalUrl(), Instant.now());

        when(urlRepository.findByOriginalUrl(request.originalUrl())).thenReturn(Optional.of(existingUrl));

        assertThatThrownBy(() -> urlService.createShortUrl(request))
                .isInstanceOf(DuplicateUrlException.class);

        verifyNoInteractions(randomBase62Generator);
    }

    @Test
    void resolvesTheUrlIncrementsClicksAndPublishesAnAnalyticsEvent() {
        UrlEntity urlEntity = new UrlEntity("A1b2C3d4", "https://example.com/page", Instant.now());
        ArgumentCaptor<UrlRedirectedEvent> eventCaptor = ArgumentCaptor.forClass(UrlRedirectedEvent.class);

        when(urlRepository.findByShortCode("A1b2C3d4")).thenReturn(Optional.of(urlEntity));

        String originalUrl = urlService.resolveOriginalUrl("A1b2C3d4");

        assertThat(originalUrl).isEqualTo("https://example.com/page");
        assertThat(urlEntity.getClickCount()).isEqualTo(1);
        verify(applicationEventPublisher).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue().shortCode()).isEqualTo("A1b2C3d4");
    }

    @Test
    void throwsNotFoundWhenTheShortCodeDoesNotExist() {
        when(urlRepository.findByShortCode("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> urlService.resolveOriginalUrl("missing"))
                .isInstanceOf(ShortUrlNotFoundException.class);
    }

    @Test
    void propagatesRepositorySaveFailure() {
        CreateShortUrlRequest request = new CreateShortUrlRequest("https://example.com/page");

        when(urlRepository.findByOriginalUrl(request.originalUrl())).thenReturn(Optional.empty());
        when(randomBase62Generator.generate(8)).thenReturn("A1b2C3d4");
        when(urlRepository.existsByShortCode("A1b2C3d4")).thenReturn(false);
        when(urlRepository.save(any(UrlEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Database unavailable"));

        assertThatThrownBy(() -> urlService.createShortUrl(request))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}