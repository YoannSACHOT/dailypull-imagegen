package fr.jixter.dailypull.imagegen.config;

import java.net.http.HttpClient;
import java.time.Duration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({OneMinAiConfig.class, ImageGenConfig.class})
public class AppConfig {

  @Bean
  public HttpClient httpClient() {
    return HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30)).build();
  }
}
