package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProps(String secretB64, Duration accessTtl, String issuer, String algo) {
}
