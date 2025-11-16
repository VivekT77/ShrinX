package com.example.url_shortener.controller;

import com.example.url_shortener.dto.ShortenUrlRequest;
import com.example.url_shortener.dto.UrlStatsResponse;
import com.example.url_shortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
public class UrlController {

    @Autowired
    private UrlShortenerService service;

    // Handle favicon to prevent errors
    @GetMapping("/favicon.ico")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void favicon() {
        // Browsers automatically request this, just return empty
    }

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody ShortenUrlRequest request) {
        String response = service.shortenUrl(request.url());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

        // Additional safety check for static resources
        if (shortCode.matches(".*\\.(ico|css|js|png|jpg|jpeg|gif|svg)$")) {
            return ResponseEntity.notFound().build();
        }

        try {
            String originalUrl = service.getOriginalUrlAndIncrementClicks(shortCode);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(originalUrl))
                    .build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<UrlStatsResponse> getStats(@PathVariable String shortCode) {
        try {
            UrlStatsResponse stats = service.getStats(shortCode);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}