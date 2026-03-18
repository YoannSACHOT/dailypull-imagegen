package fr.jixter.dailypull.imagegen.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record BatchGenerationRequest(
    @NotEmpty @Valid List<ImageGenerationRequest> requests, boolean downloadLocally) {}
