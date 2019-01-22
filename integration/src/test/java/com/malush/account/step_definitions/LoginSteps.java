package com.malush.account.step_definitions;

import com.malush.account.repository.UserRepository;
import com.malush.account.requests.ApiPath;
import com.malush.account.requests.LoginRequest;
import cucumber.api.java8.En;
import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import static org.junit.Assert.*;

public class LoginSteps implements En {

  private Response response;
  private LoginRequest loginRequestBody;


  public LoginSteps() {

    Before(() -> {
      UserRepository.getRepository().deleteAll();
    });

    After(() -> {
      response = null;
      loginRequestBody = null;
    });
    
    Given("the user inserts login credentials: {string} and {string}", (String name, String password) -> {
      loginRequestBody = createLoginRequestBody(name, password);
    });

    When("the user requests to login", () -> {
      response =
        given().
          contentType(ContentType.JSON).
          body(loginRequestBody).
        when().post(ApiPath.ACCESS_TOKENS);
    });

    Given("an unknown user inserts login credentials", () -> {
      loginRequestBody = createLoginRequestBody("unknown", "unknown");
    });

    Then("the access is granted", () -> {
      response.then().statusCode(HttpStatus.SC_OK);
      assertNotNull(response.jsonPath().get("access_token"));
    });

    When("a user successfully signs up", () -> {
      response =
        given().
          contentType(ContentType.JSON).
          body(createLoginRequestBody("malush", "qwerty123")).
        when().
          post(ApiPath.SIGN_UP).
        then().
          statusCode(HttpStatus.SC_CREATED).extract().response();
    });

    Then("the user is immediately granted access to the system", () -> {
      assertNotNull(response.jsonPath().get("access_token"));
    });

    Then("the access is forbidden", () -> {
      response.then().statusCode(HttpStatus.SC_FORBIDDEN);
    });
  }

  private LoginRequest createLoginRequestBody(String name, String password) {
    return new LoginRequest(name.equals("null") ? null : name, password.equals("null") ? null : password);
  }
}
