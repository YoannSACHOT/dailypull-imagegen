package fr.jixter.dailypull.imagegen.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "imagegen")
public record ImageGenConfig(String outputDir) {}
