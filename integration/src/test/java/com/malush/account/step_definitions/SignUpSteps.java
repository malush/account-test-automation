package com.malush.account.step_definitions;

import com.malush.account.model.User;
import com.malush.account.repository.UserRepository;
import com.malush.account.util.TestUtil;
import cucumber.api.Scenario;
import cucumber.api.java8.En;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;

public class SignUpSteps implements En {

  private RequestSpecification request;
  private Response response;
  public SignUpSteps() {

    After(() -> {
      UserRepository.getRepository().deleteAll();
    });

    When("a user tries to register a new profile with valid data", () -> {
      response =
        given().
            contentType(ContentType.JSON).
            body(new User("malush", "malush")).
        when().
            post("/sign-up");
    });

    Then("the user sign up is successful", () -> {
      response.then().statusCode(201).log().all();
    });


  }
}
