package com.malush.account;

import com.malush.account.repository.AccountRepository;
import com.malush.account.repository.UserRepository;
import com.malush.util.settings.SettingsUtil;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import io.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty",
              "json:build/export-results/cucumber.json",
              "junit:build/export-results/cucumber_junit.xml"},
    features = {"src/test/resources/com/malush/account/features"},
    glue = {"com.malush.account.step_definitions"}
)
public class TestRunner {

  @BeforeClass
  public static void setup() {
    RestAssured.port = SettingsUtil.getSettings().getWebConfig().getPort();
    RestAssured.baseURI = SettingsUtil.getSettings().getWebConfig().getBaseUri();
  }

  @AfterClass
  public static void shutdown() {
    AccountRepository.getRepository().deleteAll();
    AccountRepository.getRepository().closeConnection();
    UserRepository.getRepository().deleteAll();
    UserRepository.getRepository().closeConnection();
  }
}
