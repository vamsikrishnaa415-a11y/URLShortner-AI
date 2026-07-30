package com.aiurlshortener.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI gatewayOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("AI URL Shortener Gateway")
                .version("v1")
                .description("Gateway Swagger UI aggregates the URL and Analytics service APIs."));
    }
}
