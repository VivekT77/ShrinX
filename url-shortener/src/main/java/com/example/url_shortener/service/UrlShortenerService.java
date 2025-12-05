package com.example.url_shortener.service;

import com.example.url_shortener.dto.UrlStatsResponse;
import com.example.url_shortener.exception.AliasAlreadyExistsException;
import com.example.url_shortener.exception.UrlNotFoundException;
import com.example.url_shortener.model.UrlMapping;
import com.example.url_shortener.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class UrlShortenerService {

    private static final String Base62_Chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final UrlMappingRepository urlMappingRepository;
    public UrlShortenerService(UrlMappingRepository urlMappingRepository){
        this.urlMappingRepository = urlMappingRepository;
    }

    @Transactional
    public String shortenUrl(String originalUrl, String customAlias,Integer hoursToExpire) {

        if (StringUtils.hasText(customAlias)) {
            if (urlMappingRepository.findByShortCode(customAlias).isPresent()) {
                throw new AliasAlreadyExistsException("Alias " + customAlias + " is already in use.");
            }

            UrlMapping newUrlMapping = new UrlMapping();
            newUrlMapping.setOriginalUrl(originalUrl);
            newUrlMapping.setCreationDate(LocalDateTime.now());
            newUrlMapping.setShortCode(customAlias);

            if(hoursToExpire != null){
                newUrlMapping.setExpirationDate(LocalDateTime.now().plusHours(hoursToExpire));
            }

            urlMappingRepository.save(newUrlMapping);
            return customAlias;
        }
        else {
            UrlMapping urlMapping = new UrlMapping();
            urlMapping.setOriginalUrl(originalUrl);
            urlMapping.setCreationDate(LocalDateTime.now());

            if(hoursToExpire != null){
                urlMapping.setExpirationDate(LocalDateTime.now().plusHours(hoursToExpire));
            }

            UrlMapping savedEntity = urlMappingRepository.save(urlMapping);

            String shortCode = encodeBase62(savedEntity.getId());
            savedEntity.setShortCode(shortCode);
            urlMappingRepository.save(savedEntity);

            return shortCode;
        }
    }

    @Transactional
    public String getOriginalUrlAndIncrementClicks(String shortCode){

        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode).orElseThrow(() -> new UrlNotFoundException
                ("Url not found for shortCode: " + shortCode));

        if(urlMapping.getExpirationDate() != null && urlMapping.getExpirationDate().isBefore(LocalDateTime.now())){
            throw new UrlNotFoundException("This link has expired and is no longer active.");
        }

        urlMapping.setClickCount(urlMapping.getClickCount() + 1);
        urlMappingRepository.save(urlMapping);
        return urlMapping.getOriginalUrl();
    }

    @Transactional(readOnly = true)
    public UrlStatsResponse getStats(String shortCode){

        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode).orElseThrow(() -> new UrlNotFoundException("No statistics found for shortCode: " + shortCode));
        String fullShortUrl = "http://localhost:8080/" + urlMapping.getShortCode();

        return new UrlStatsResponse(
                urlMapping.getOriginalUrl(),
                fullShortUrl,
                urlMapping.getCreationDate(),
                urlMapping.getClickCount()
        );
    }

    private String encodeBase62(Long number){
        if(number==0){
            return String.valueOf(Base62_Chars.charAt(0));
        }

        StringBuilder sb = new StringBuilder();
        long num = number;

        while (num>0){
            int remainder = (int)(num % 62);
            sb.append(Base62_Chars.charAt(remainder));
            num /= 62;
        }
        return sb.reverse().toString();
    }
}
