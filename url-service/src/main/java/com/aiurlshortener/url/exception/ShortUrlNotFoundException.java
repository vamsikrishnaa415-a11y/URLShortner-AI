package com.aiurlshortener.url.exception;

/**
 * Indicates that a requested short code does not have a URL mapping.
 */
public class ShortUrlNotFoundException extends RuntimeException {

    /**
     * Creates an exception for a missing short code.
     *
     * @param shortCode missing short code
     */
    public ShortUrlNotFoundException(String shortCode) {
        super("Short URL not found: " + shortCode);
    }
}