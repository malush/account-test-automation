package com.malush.account.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class TestSupport {
  private static Environments environments;

  public static Environments getEnvironments()
  {
    if(environments == null)
      loadSettings();
    return environments;
  }

  private static synchronized void loadSettings() {
    if (environments != null)
      return;

    Constructor constructor = new Constructor(Settings.class);
    Yaml yaml = new Yaml(constructor);

    try (InputStream inputStream = TestSupport.class.getClassLoader().getResourceAsStream("settings.yaml")) {
      Optional.of(yaml.loadAs(inputStream, Settings.class))
          .ifPresent(settings -> {
            environments = settings.getEnvironments();
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
