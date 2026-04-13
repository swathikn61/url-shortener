package com.example.url_shortener.dto.response;

import com.example.url_shortener.model.UrlMapping;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UrlResponse {

    private String originalUrl;
    private String shortUrl;
    private Long clicks;
    private LocalDateTime createdAt;

    public static UrlResponse mapToDto(UrlMapping entity) {
        return UrlResponse.builder()
                .originalUrl(entity.getOriginalUrl())
                .shortUrl("http://localhost:8080/" + entity.getShortCode())
                .clicks(entity.getClickCount())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}