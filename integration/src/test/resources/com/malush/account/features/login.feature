Feature: Login
  As an existing user of the system
  I want to be able to login and have access to the system

  Scenario: Successful login
    Given the user already exists in the system
    When the user requests to login and have the full access to the system
    Then the access is granted

  Scenario: Login after sign up
    When a user successfully signs up
    Then the user is immediately granted access to the system

  Scenario: User doesn't exist
    When the user requests to login and have the full access to the system
    Then the access is forbidden

  Scenario: Wrong password
    When the user requests to login with a wrong password
    Then the access is forbidden

  Scenario Outline: Missing fields
    When the user requests to login with missing "<inputData>"
    Then the access is forbidden
    Examples:
      |inputData |
      |name      |
      |password  |
      |all fields|