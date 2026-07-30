package com.aiurlshortener.url.service;

import com.aiurlshortener.url.dto.CreateShortUrlRequest;
import com.aiurlshortener.url.dto.CreateShortUrlResponse;

public interface UrlService {

    CreateShortUrlResponse createShortUrl(CreateShortUrlRequest request);

    String resolveOriginalUrl(String shortCode);
}