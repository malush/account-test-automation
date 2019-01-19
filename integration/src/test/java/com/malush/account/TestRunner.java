package com.malush.account;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
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
}
