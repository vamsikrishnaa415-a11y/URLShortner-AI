package com.aiurlshortener.url.controller;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aiurlshortener.url.dto.CreateShortUrlResponse;
import com.aiurlshortener.url.exception.DuplicateUrlException;
import com.aiurlshortener.url.exception.GlobalExceptionHandler;
import com.aiurlshortener.url.exception.ShortUrlNotFoundException;
import com.aiurlshortener.url.service.UrlService;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UrlController.class)
@Import(GlobalExceptionHandler.class)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

        @MockitoBean
    private UrlService urlService;

    @Test
    void createsShortUrlSuccessfully() throws Exception {
        when(urlService.createShortUrl(any())).thenReturn(new CreateShortUrlResponse(
                "A1b2C3d4", "https://example.com/page", Instant.parse("2026-07-30T10:00:00Z"), true
        ));

        mockMvc.perform(post("/api/v1/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"originalUrl\":\"https://example.com/page\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortCode").value("A1b2C3d4"))
                .andExpect(jsonPath("$.originalUrl").value("https://example.com/page"))
                .andExpect(jsonPath("$.created").value(true));
    }

    @Test
    void rejectsInvalidUrl() throws Exception {
        mockMvc.perform(post("/api/v1/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"originalUrl\":\"ftp://example.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors.originalUrl").exists());

        verifyNoInteractions(urlService);
    }

    @Test
    void rejectsEmptyUrl() throws Exception {
        mockMvc.perform(post("/api/v1/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"originalUrl\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verifyNoInteractions(urlService);
    }

    @Test
    void returnsConflictForDuplicateUrl() throws Exception {
        doThrow(new DuplicateUrlException("https://example.com/page"))
                .when(urlService).createShortUrl(any());

        mockMvc.perform(post("/api/v1/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"originalUrl\":\"https://example.com/page\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void redirectsExistingUrl() throws Exception {
        when(urlService.resolveOriginalUrl("A1b2C3d4")).thenReturn("https://example.com/page");

        mockMvc.perform(get("/A1b2C3d4"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", endsWith("/page")));
    }

    @Test
    void returnsNotFoundForUnknownShortCode() throws Exception {
        doThrow(new ShortUrlNotFoundException("A1b2C3d4"))
                .when(urlService).resolveOriginalUrl("A1b2C3d4");

        mockMvc.perform(get("/A1b2C3d4"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}