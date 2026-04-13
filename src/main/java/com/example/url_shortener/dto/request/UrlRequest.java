package com.example.url_shortener.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlRequest {
    private String url;

    //jakarta validations
    private Integer expiryInDays;
    private String customCode;

    private boolean validateUrl() {
        return true;
    }
}

