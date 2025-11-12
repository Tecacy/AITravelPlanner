package com.ai.travelplanner.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "app.security.cors")
public class CorsProperties {

    /**
     * Comma separated origin list.
     */
    private String allowedOrigins = "http://localhost:5173";

    /**
     * Comma separated method list.
     */
    private String allowedMethods = "GET,POST,PUT,DELETE,OPTIONS";

    /**
     * Comma separated header list or wildcard.
     */
    private String allowedHeaders = "*";

    public List<String> getAllowedOrigins() {
        return splitAndTrim(allowedOrigins);
    }

    public void setAllowedOrigins(String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public List<String> getAllowedMethods() {
        return splitAndTrim(allowedMethods);
    }

    public void setAllowedMethods(String allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public List<String> getAllowedHeaders() {
        return splitAndTrim(allowedHeaders);
    }

    public void setAllowedHeaders(String allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    private List<String> splitAndTrim(String value) {
        if (!StringUtils.hasText(value)) {
            return List.of();
        }
        return StringUtils.commaDelimitedListToSet(value).stream()
                .map(String::trim)
                .filter(StringUtils::hasText)
                .toList();
    }
}


