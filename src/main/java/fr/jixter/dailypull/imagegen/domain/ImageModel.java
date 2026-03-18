package fr.jixter.dailypull.imagegen.domain;

public enum ImageModel {
  DALL_E_3("dall-e-3", "DALL-E 3 (OpenAI)"),
  DALL_E_2("dall-e-2", "DALL-E 2 (OpenAI)"),
  GPT_IMAGE_1("gpt-image-1", "GPT Image 1 (OpenAI)"),
  FLUX_PRO_1_1("black-forest-labs/flux-1.1-pro", "Flux Pro 1.1"),
  FLUX_DEV("black-forest-labs/flux-dev", "Flux Dev"),
  FLUX_SCHNELL("black-forest-labs/flux-schnell", "Flux Schnell"),
  STABLE_IMAGE("stable-image", "Stable Image Core"),
  GEMINI_3_PRO("gemini-3-pro-image-preview", "Gemini 3 Pro Image"),
  GEMINI_FLASH("gemini-3-1-flash-image-preview", "Gemini 3.1 Flash Image");

  private final String apiId;
  private final String displayName;

  ImageModel(String apiId, String displayName) {
    this.apiId = apiId;
    this.displayName = displayName;
  }

  public String getApiId() {
    return apiId;
  }

  public String getDisplayName() {
    return displayName;
  }
}
