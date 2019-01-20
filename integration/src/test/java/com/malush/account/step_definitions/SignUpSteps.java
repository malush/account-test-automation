package com.malush.account.step_definitions;

import com.malush.account.repository.UserRepository;
import com.malush.account.util.Settings;
import com.malush.account.util.TestSupport;
import cucumber.api.java8.En;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class SignUpSteps implements En {

  private RequestSpecification request;
  private Response response;
  public SignUpSteps() {

    When("a user tries to register a new profile with valid data", () -> {
      UserRepository.getRepository().deleteAll();
      RestAssured.port = 8095;
      RestAssured.baseURI = "http://localhost";
      response =
        given().
            contentType(ContentType.JSON).
            body("{\n"
                + "  \"name\": \"Malush3\",\n"
                + "  \"password\": \"malush\"\n"
                + "}").
        when().
            post("/sign-up");

    });

    Then("the user sign up is successful", () -> {
      response.then().statusCode(201).log().all();
      //userRepository.deleteAll();
    });
  }
}
