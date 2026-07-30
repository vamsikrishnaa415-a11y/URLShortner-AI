package com.aiurlshortener.url.exception;

public class DuplicateUrlException extends RuntimeException {

    public DuplicateUrlException(String originalUrl) {
        super("A short URL already exists for: " + originalUrl);
    }
}