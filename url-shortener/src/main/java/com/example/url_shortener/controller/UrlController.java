package com.example.url_shortener.controller;

import com.example.url_shortener.dto.ShortenUrlRequest;
import com.example.url_shortener.dto.ShortenUrlResponse;
import com.example.url_shortener.dto.UrlStatsResponse;
import com.example.url_shortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
public class UrlController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    public UrlController(UrlShortenerService urlShortenerService){
        this.urlShortenerService = urlShortenerService;
    }

    // Handle favicon to prevent errors
    @GetMapping("/favicon.ico")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void favicon() {
        // Browsers automatically request this, just return empty
    }

    @PostMapping("/api/v1/url/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request) {

        String shortCode = urlShortenerService.shortenUrl(request.url(), request.customAlias(), request.hoursToExpire());
        String fullShortUrl = "http://localhost:8080/" + shortCode;
        ShortenUrlResponse response = new ShortenUrlResponse(fullShortUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

        // Additional safety check for static resources
        if (shortCode.matches(".*\\.(ico|css|js|png|jpg|jpeg|gif|svg)$")) {
            return ResponseEntity.notFound().build();
        }

        try {
            String originalUrl = urlShortenerService.getOriginalUrlAndIncrementClicks(shortCode);
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
            UrlStatsResponse stats = urlShortenerService.getStats(shortCode);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}