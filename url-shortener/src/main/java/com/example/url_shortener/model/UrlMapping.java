package com.example.url_shortener.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String originalUrl;

    @Column(unique = true)
    private String shortCode;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private int clickCount = 0;

    private LocalDateTime expirationDate;
    //Getters
    public Long getId() {
        return id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public long getClickCount() {
        return clickCount;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    //Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setClickCount(long clickCount) {
        this.clickCount = clickCount;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}
