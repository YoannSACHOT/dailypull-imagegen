package fr.jixter.dailypull.imagegen;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
    properties = {
      "onemin.api.base-url=https://api.1min.ai",
      "onemin.api.key=test-key",
      "imagegen.output-dir=./test-output"
    })
class DailyPullImageGenApplicationTests {

  @Test
  void contextLoads() {}
}
