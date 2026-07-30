package com.aiurlshortener.url.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aiurlshortener.url.entity.UrlEntity;
import com.aiurlshortener.url.repository.UrlRepository;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UrlServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UrlRepository urlRepository;

    @BeforeEach
    void clearDatabase() {
        urlRepository.deleteAll();
    }

    @Test
    void createsShortUrlAndPersistsItInH2() throws Exception {
        mockMvc.perform(post("/api/v1/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"originalUrl\":\"https://example.com/page\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortCode").isString())
                .andExpect(jsonPath("$.originalUrl").value("https://example.com/page"));

        assertThat(urlRepository.findByOriginalUrl("https://example.com/page")).isPresent();
    }

    @Test
    void redirectsExistingShortCodeWithLocationHeader() throws Exception {
        urlRepository.save(new UrlEntity(
                "A1b2C3d4", "https://example.com/page", Instant.parse("2026-07-30T10:00:00Z")
        ));

        mockMvc.perform(get("/A1b2C3d4"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://example.com/page"));

        assertThat(urlRepository.findByShortCode("A1b2C3d4").orElseThrow().getClickCount()).isEqualTo(1);
    }
}