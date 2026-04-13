package com.example.url_shortener.controller;

import com.example.url_shortener.dto.request.UrlRequest;
import com.example.url_shortener.dto.response.ApiResponse;
import com.example.url_shortener.dto.response.UrlResponse;
import com.example.url_shortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class UrlController {

    private final UrlService service;

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> shorten(
            @RequestBody UrlRequest request) {
        return ResponseEntity.ok(service.createShortUrl(request));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

        String url = service.redirect(shortCode);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    @GetMapping("/info/{shortCode}")
    public ResponseEntity<UrlResponse> info(
            @PathVariable String shortCode) {

        return ResponseEntity.ok(service.getInfo(shortCode));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> stats() {
        return ResponseEntity.ok(service.getStats());
    }

    @GetMapping("/urls")
    public ResponseEntity<Page<UrlResponse>> getAll(
            @RequestParam int page,
            @RequestParam int size) {
        return ResponseEntity.ok(service.getAllUrls(page, size));
    }
}