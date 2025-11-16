package com.example.url_shortener.controller;

import com.example.url_shortener.dto.UrlStatsResponse;
import com.example.url_shortener.exception.UrlNotFoundException;
import com.example.url_shortener.service.UrlShortenerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PageController {
    private final UrlShortenerService urlShortenerService;
    public PageController(UrlShortenerService urlShortenerService){
        this.urlShortenerService = urlShortenerService;
    }
    @GetMapping("/")
    public String home(){

        return "index";
    }

    @PostMapping("/shorten-web")
    public String handleShortenForm(@RequestParam ("longUrl") String longUrl, Model model){
        try{
            String shortCode = urlShortenerService.shortenUrl(longUrl);

            String fullShortUrl = "http://localhost:8080/api/v1/" + shortCode;
            model.addAttribute("originalUrl", longUrl);
            model.addAttribute("shortUrlResult", fullShortUrl);
        }
        catch (Exception e){
            model.addAttribute("Error","Failed to shorten URL:" + e.getMessage());
        }
        return "index";
    }

    @PostMapping("/check-stats")
    public String handleStatsCheckForm(@RequestParam("checkShortCode") String shortCode, Model model){

        try{
            UrlStatsResponse stats = urlShortenerService.getStats(shortCode);
            model.addAttribute("urlStats", stats);

        }catch (UrlNotFoundException e){
            model.addAttribute("statsError", "Statistics not found for short code:" + shortCode);
        }catch (Exception e){
            model.addAttribute("statsError", "Error retrieving stats: " + e.getMessage());
        }
        return "index";
    }
}
