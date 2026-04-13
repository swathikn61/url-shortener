package com.example.url_shortener.service;

import com.example.url_shortener.dto.request.UrlRequest;
import com.example.url_shortener.dto.response.UrlResponse;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface UrlService {
    UrlResponse createShortUrl(UrlRequest request);
    String redirect(String shortCode);
    UrlResponse getInfo(String shortCode);
    Map<String, Long> getStats();
    Page<UrlResponse> getAllUrls(int page, int size);
}