package com.aiurlshortener.url.exception;

/**
 * Indicates that a unique short code could not be generated within the configured attempt limit.
 */
public class ShortCodeGenerationException extends RuntimeException {

    /**
     * Creates a short-code generation failure.
     */
    public ShortCodeGenerationException() {
        super("Unable to generate a unique short code");
    }
}