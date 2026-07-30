package com.aiurlshortener.url.exception;

/**
 * Indicates that a requested destination already has a short URL mapping.
 */
public class DuplicateUrlException extends RuntimeException {

    /**
     * Creates an exception for the duplicate destination.
     *
     * @param originalUrl duplicate destination URL
     */
    public DuplicateUrlException(String originalUrl) {
        super("A short URL already exists for: " + originalUrl);
    }
}