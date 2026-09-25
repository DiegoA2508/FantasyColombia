package com.fantasy.ligabetplay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api-football")
public record ApiFootballProperties(
        String baseUrl,
        String apiKey,
        Integer ligaBetplayId
) {
}
