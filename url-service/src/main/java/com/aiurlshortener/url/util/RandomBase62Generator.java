package com.aiurlshortener.url.util;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
/**
 * Generates cryptographically strong Base62 identifiers for short URLs.
 */
public class RandomBase62Generator {

    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generates a Base62 identifier of the requested length.
     *
     * @param length requested identifier length
     * @return generated Base62 identifier
     */
    public String generate(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length must be positive");
        }

        char[] shortCode = new char[length];
        for (int index = 0; index < length; index++) {
            shortCode[index] = BASE62[secureRandom.nextInt(BASE62.length)];
        }
        return new String(shortCode);
    }
}