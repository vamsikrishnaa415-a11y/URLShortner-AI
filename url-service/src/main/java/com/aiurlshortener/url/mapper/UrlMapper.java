package com.aiurlshortener.url.mapper;

import com.aiurlshortener.url.dto.CreateShortUrlResponse;
import com.aiurlshortener.url.entity.UrlEntity;
import org.springframework.stereotype.Component;

@Component
/**
 * Maps URL persistence entities to API response models.
 */
public class UrlMapper {

    /**
     * Maps a URL entity to a creation response.
     *
     * @param urlEntity persisted URL mapping
     * @param created whether the mapping was created in this request
     * @return API response model
     */
    public CreateShortUrlResponse toCreateShortUrlResponse(UrlEntity urlEntity, boolean created) {
        return new CreateShortUrlResponse(
                urlEntity.getShortCode(),
                urlEntity.getOriginalUrl(),
                urlEntity.getCreatedAt(),
                created
        );
    }
}