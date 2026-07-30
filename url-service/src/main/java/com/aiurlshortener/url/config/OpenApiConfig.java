package com.aiurlshortener.url.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI urlServiceOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("URL Service API")
                .version("v1")
                .description("Creates and resolves shortened URLs."));
    }
}
