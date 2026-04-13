package com.example.url_shortener.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ShortCodeGenerator {
    private static final String BASE62="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final Random random=new Random();
    public String generate(){
        StringBuilder code=new StringBuilder();
        for(int i=0;i<6;i++){
            code.append(BASE62.charAt(random.nextInt(BASE62.length())));
        }
        return code.toString();
    }
}