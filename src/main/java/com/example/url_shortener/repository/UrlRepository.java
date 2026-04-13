package com.example.url_shortener.repository;

import com.example.url_shortener.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
    Optional<UrlMapping> findByOriginalUrl(String originalUrl);
}
