package fr.jixter.dailypull.imagegen.domain;

import java.time.Instant;

public record ImageGenerationResult(
    String uuid,
    String model,
    String status,
    String imageUrl,
    String localPath,
    Instant createdAt) {}
