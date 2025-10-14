package com.example.url_shortener.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public record ShortenUrlRequest(
        @NotEmpty(message = "Url cannot be empty")
        @URL(message = "A valid URL format is required")
        String url
) {
}
