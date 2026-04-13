package com.example.url_shortener.service.impl;


import com.example.url_shortener.exception.BadRequestException;
import com.example.url_shortener.exception.ResourceNotFoundException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.PageRequest;
import com.example.url_shortener.dto.request.UrlRequest;
import com.example.url_shortener.dto.response.UrlResponse;
import com.example.url_shortener.model.UrlMapping;
import com.example.url_shortener.repository.UrlRepository;
import com.example.url_shortener.service.UrlService;
import com.example.url_shortener.util.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "urls")
public class UrlServiceImpl implements UrlService {

    private final UrlRepository repository;
    private final ShortCodeGenerator generator;

    @Override
    public UrlResponse createShortUrl(UrlRequest request) {

        if (!request.getUrl().startsWith("http")) {
            throw new BadRequestException("Invalid URL");
        }

        String code;

        if (request.getCustomCode() != null && !request.getCustomCode().isBlank()) {

            if (repository.existsByShortCode(request.getCustomCode())) {
                throw new BadRequestException("Custom code already exists");
            }

            code = request.getCustomCode();

        } else {

            do {
                code = generator.generate();
            } while (repository.existsByShortCode(code));
        }

        UrlMapping mapping = UrlMapping.builder()
                .originalUrl(request.getUrl())
                .shortCode(code)
                .clickCount(0L)
                .createdAt(LocalDateTime.now())
                .expiryAt(request.getExpiryInDays() != null
                        ? LocalDateTime.now().plusDays(request.getExpiryInDays())
                        : null)
                .build();

        repository.save(mapping);

        return UrlResponse.mapToDto(mapping);
    }

    @Override
    public String redirect(String shortCode) {

        UrlMapping mapping = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("URL not found"));

        if (mapping.getExpiryAt() != null &&
                mapping.getExpiryAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Link expired");
        }

        mapping.setClickCount(mapping.getClickCount() + 1);
        repository.save(mapping);

        return mapping.getOriginalUrl();
    }

    @Override
    public UrlResponse getInfo(String shortCode) {

        UrlMapping mapping = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        return UrlResponse.mapToDto(mapping);
    }

    @Override
    public Map<String, Long> getStats() {

        long totalUrls = repository.count();
        long totalClicks = repository.findAll()
                .stream()
                .mapToLong(UrlMapping::getClickCount)
                .sum();

        return Map.of(
                "totalUrls", totalUrls,
                "totalClicks", totalClicks
        );
    }

    @Override
    public Page<UrlResponse> getAllUrls(int page, int size) {

        Page<UrlMapping> mappings =
                repository.findAll(PageRequest.of(page, size));

        return mappings.map(UrlResponse::mapToDto);
    }
}