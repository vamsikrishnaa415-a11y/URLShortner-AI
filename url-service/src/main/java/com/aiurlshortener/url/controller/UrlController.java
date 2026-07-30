package com.aiurlshortener.url.controller;

import com.aiurlshortener.url.dto.CreateShortUrlRequest;
import com.aiurlshortener.url.dto.CreateShortUrlResponse;
import com.aiurlshortener.url.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
/**
 * Exposes URL creation and redirect HTTP endpoints.
 */
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/api/v1/urls")
    /**
     * Creates a unique short URL mapping.
     *
     * @param request validated destination URL request
     * @return the created mapping and its HTTP status
     */
    public ResponseEntity<CreateShortUrlResponse> createShortUrl(@Valid @RequestBody CreateShortUrlRequest request) {
        CreateShortUrlResponse response = urlService.createShortUrl(request);
        HttpStatus status = response.created() ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/{shortCode}")
    /**
     * Redirects a client to the destination associated with a short code.
     *
     * @param shortCode short code to resolve
     * @return a 302 response with a Location header
     */
    public ResponseEntity<Void> redirect(@PathVariable("shortCode") String shortCode) {
        String originalUrl = urlService.resolveOriginalUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, originalUrl) 
                .build();
    }
}