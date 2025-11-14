package com.example.url_shortener.controller;

import com.example.url_shortener.service.UrlShortenerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
    private final UrlShortenerService urlShortenerService;
    public PageController(UrlShortenerService urlShortenerService){
        this.urlShortenerService = urlShortenerService;
    }
    @GetMapping("/")
    public  String indexPage(){
        return "index";
    }
    @PostMapping("/Shorten-web")
    public String handleShortenForm(@RequestParam ("longUrl") String longUrl, Model model){

        String shortCode = urlShortenerService.shortenUrl(longUrl);

        String fullShortUrl = "Https://localhost:8080/" + shortCode;
        model.addAttribute("originalUrl", longUrl);
        model.addAttribute("shortUrlResult", fullShortUrl);
        return "index";
    }

}
