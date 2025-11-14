package com.example.url_shortener.controller;

import com.example.url_shortener.dto.ShortenUrlRequest;
import com.example.url_shortener.dto.ShortenUrlResponse;
import com.example.url_shortener.dto.UrlStatsResponse;
import com.example.url_shortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlController {
    private final UrlShortenerService urlShortenerService;
    public UrlController(UrlShortenerService urlShortenerService) {

        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/api/v1/url/Shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request){

        String shortCode = urlShortenerService.shortenUrl(request.url());

        String fullShortUrl = "https://localhost:8080/" + shortCode;

        ShortenUrlResponse response = new ShortenUrlResponse(fullShortUrl);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect (@PathVariable String shortCode){

        String originalUrl = urlShortenerService.getOriginalUrlAndIncrementClicks(shortCode);

//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create(originalUrl));
//        return new ResponseEntity<>(headers, HttpStatus.FOUND);
        //same logic as above but with "fluent design patter"
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
    }

    @GetMapping("/api/v1/url/stats/{shortCode}")
    public ResponseEntity<UrlStatsResponse> getUrlStats(@PathVariable String shortCode){
        return null;
    }
}
