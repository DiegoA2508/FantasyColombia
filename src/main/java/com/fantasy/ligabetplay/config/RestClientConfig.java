package com.fantasy.ligabetplay.config;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient apiFootballRestClient(ApiFootballProperties properties) {
        String baseUrl = Objects.requireNonNull(
            properties.baseUrl(), "api-football.base-url must be configured");
        String apiKey = Objects.requireNonNull(
            properties.apiKey(), "api-football.api-key must be configured");

        return RestClient.builder()
            .baseUrl(baseUrl)
                // Header correcto para suscripcion directa en API-Sports.
            .defaultHeader("x-apisports-key", apiKey)
                .build();
    }
}
