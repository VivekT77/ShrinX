package com.example.url_shortener.controller;

import com.example.url_shortener.dto.UrlStatsResponse;
import com.example.url_shortener.exception.AliasAlreadyExistsException;
import com.example.url_shortener.exception.UrlNotFoundException;
import com.example.url_shortener.service.UrlShortenerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Controller
public class PageController {
    private final UrlShortenerService urlShortenerService;

    @Value("${app.base-url}")
    private String baseUrl;
    public PageController(UrlShortenerService urlShortenerService){
        this.urlShortenerService = urlShortenerService;
    }

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @PostMapping("/shorten-web")
    public String handleShortenForm(@RequestParam ("longUrl") String longUrl, @RequestParam (name= "customAlias", required = false) String customAlias,  Model model){

        model.addAttribute("originalUrl", longUrl);

        try{
            String shortCode = urlShortenerService.shortenUrl(longUrl, customAlias, null);
            String fullShortUrl = baseUrl + "/" + shortCode;
            model.addAttribute("shortUrlResult", fullShortUrl);
        }
        catch (AliasAlreadyExistsException e){
            model.addAttribute("aliasError", e.getMessage());
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
        }
        return "index";
    }
}
