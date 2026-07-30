package com.aiurlshortener.url.mapper;

import com.aiurlshortener.url.dto.CreateShortUrlResponse;
import com.aiurlshortener.url.entity.UrlEntity;
import org.springframework.stereotype.Component;

@Component
public class UrlMapper {

    public CreateShortUrlResponse toCreateShortUrlResponse(UrlEntity urlEntity, boolean created) {
        return new CreateShortUrlResponse(
                urlEntity.getShortCode(),
                urlEntity.getOriginalUrl(),
                urlEntity.getCreatedAt(),
                created
        );
    }
}