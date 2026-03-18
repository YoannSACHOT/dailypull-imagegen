package fr.jixter.dailypull.imagegen.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "onemin.api")
public record OneMinAiConfig(String baseUrl, String key) {}
