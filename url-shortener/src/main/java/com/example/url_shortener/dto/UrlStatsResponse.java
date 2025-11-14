package com.example.url_shortener.dto;

import java.time.LocalDateTime;

public record UrlStatsResponse(
        String originalUrl,
        String shortUrl,
        LocalDateTime creationDate,
        long clickCount
) {}
