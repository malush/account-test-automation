Feature: Sign up
  As a new user of the system
  I want to be able to register a user profile with chosen credentials


  Scenario: Successful sign up
    When a user tries to register a new profile with valid data
    Then the user sign up is successful

  Scenario Outline: Missing fields
    When the user tries to register a new profile with missing "<inputData>"
    Then the user sign up fails with Bad Request response
    Examples:
    |inputData |
    |name      |
    |password  |

  Scenario: User sign up fails for existing users
    Given the user already exists in the system
    When a user tries to register a new profile with valid data
    Then the user sign up fails with response indicating the conflict