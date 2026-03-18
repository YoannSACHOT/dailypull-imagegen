package fr.jixter.dailypull.imagegen.client;

import fr.jixter.dailypull.imagegen.config.OneMinAiConfig;
import fr.jixter.dailypull.imagegen.domain.ImageGenerationRequest;
import fr.jixter.dailypull.imagegen.domain.ImageGenerationResult;
import fr.jixter.dailypull.imagegen.domain.ImageModel;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@Component
public class OneMinAiClient {

  private static final Logger log = LoggerFactory.getLogger(OneMinAiClient.class);
  private static final String FEATURES_ENDPOINT = "/api/features";

  private final HttpClient httpClient;
  private final OneMinAiConfig config;
  private final ObjectMapper objectMapper;

  public OneMinAiClient(HttpClient httpClient, OneMinAiConfig config, ObjectMapper objectMapper) {
    this.httpClient = httpClient;
    this.config = config;
    this.objectMapper = objectMapper;
  }

  public ImageGenerationResult generateImage(ImageGenerationRequest request)
      throws IOException, InterruptedException {
    String requestBody = buildRequestBody(request);

    log.info(
        "Envoi requête génération image [model={}, prompt={}...]",
        request.model().getApiId(),
        request.prompt().substring(0, Math.min(50, request.prompt().length())));

    HttpRequest httpRequest =
        HttpRequest.newBuilder()
            .uri(URI.create(config.baseUrl() + FEATURES_ENDPOINT))
            .header("API-KEY", config.key())
            .header("Content-Type", "application/json")
            .timeout(Duration.ofMinutes(3))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

    HttpResponse<String> response =
        httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200) {
      throw new IOException(
          "Erreur API 1minai (HTTP %d): %s".formatted(response.statusCode(), response.body()));
    }

    return parseResponse(response.body(), request.model());
  }

  public byte[] downloadImage(String imageUrl) throws IOException, InterruptedException {
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(imageUrl))
            .timeout(Duration.ofMinutes(2))
            .GET()
            .build();

    HttpResponse<byte[]> response =
        httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

    if (response.statusCode() != 200) {
      throw new IOException(
          "Erreur téléchargement image (HTTP %d)".formatted(response.statusCode()));
    }

    return response.body();
  }

  private String buildRequestBody(ImageGenerationRequest request) throws IOException {
    ObjectNode root = objectMapper.createObjectNode();
    root.put("type", "IMAGE_GENERATOR");
    root.put("model", request.model().getApiId());

    ObjectNode promptObject = objectMapper.createObjectNode();
    promptObject.put("prompt", request.prompt());

    switch (request.model()) {
      case DALL_E_3 -> {
        promptObject.put("n", 1);
        promptObject.put("size", request.size());
        promptObject.put("quality", request.quality());
        promptObject.put("style", request.style());
      }
      case DALL_E_2 -> {
        promptObject.put("n", 1);
        promptObject.put("size", request.size());
      }
      case GPT_IMAGE_1 -> {
        promptObject.put("n", 1);
        promptObject.put("size", request.size());
        promptObject.put("quality", request.quality());
        promptObject.put("style", request.style());
        promptObject.put("output_format", request.outputFormat());
        promptObject.put("background", "opaque");
      }
      case FLUX_PRO_1_1, FLUX_DEV, FLUX_SCHNELL -> {
        promptObject.put("aspect_ratio", sizeToAspectRatio(request.size()));
        promptObject.put("output_quality", 90);
      }
      case STABLE_IMAGE -> {
        promptObject.put("stable_image_core_aspect_ratio", sizeToAspectRatio(request.size()));
        promptObject.put("stable_image_core_output_format", request.outputFormat());
      }
      case GEMINI_3_PRO, GEMINI_FLASH -> {
        promptObject.put("imageSize", "2K");
      }
    }

    root.set("promptObject", promptObject);
    return objectMapper.writeValueAsString(root);
  }

  private ImageGenerationResult parseResponse(String responseBody, ImageModel model)
      throws IOException {
    JsonNode root = objectMapper.readTree(responseBody);
    JsonNode aiRecord = root.path("aiRecord");

    String uuid = aiRecord.path("uuid").asText();
    String status = aiRecord.path("status").asText();
    String imageUrl = aiRecord.path("temporaryUrl").asText(null);

    if (imageUrl == null || imageUrl.isBlank()) {
      JsonNode resultObject = aiRecord.path("aiRecordDetail").path("resultObject");
      if (resultObject.isArray() && !resultObject.isEmpty()) {
        imageUrl = resultObject.get(0).asText();
      }
    }

    log.info("Image générée [uuid={}, status={}, url={}]", uuid, status, imageUrl);

    return new ImageGenerationResult(uuid, model.getApiId(), status, imageUrl, null, Instant.now());
  }

  private String sizeToAspectRatio(String size) {
    return switch (size) {
      case "1024x1024", "512x512" -> "1:1";
      case "1024x1792", "1024x1536" -> "9:16";
      case "1792x1024", "1536x1024" -> "16:9";
      default -> "1:1";
    };
  }
}
