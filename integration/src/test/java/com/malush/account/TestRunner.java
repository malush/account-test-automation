package com.malush.account;

import com.malush.account.repository.UserRepository;
import com.malush.account.util.TestUtil;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
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
    RestAssured.port = TestUtil.getSettings().getWebConfig().getPort();
    RestAssured.baseURI = TestUtil.getSettings().getWebConfig().getBaseUri();
    RestAssured.config = RestAssured.config().httpClient(HttpClientConfig.httpClientConfig().reuseHttpClientInstance());
  }

  @AfterClass
  public static void shutdown() {
    UserRepository.getRepository().closeConnection();
  }
}
