package com.malush.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class TestUtil {
  private static Settings settings;

  public static Settings getSettings()
  {
    if(settings == null)
      loadSettings();
    return settings;
  }

  private static synchronized void loadSettings() {
    if (settings != null)
      return;

    Constructor constructor = new Constructor(Settings.class);
    Yaml yaml = new Yaml(constructor);

    try (InputStream inputStream = TestUtil.class.getClassLoader().getResourceAsStream("settings.yaml")) {
      Optional.of(yaml.loadAs(inputStream, Settings.class))
          .ifPresent(parsedSettings -> {
            settings = parsedSettings;
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
