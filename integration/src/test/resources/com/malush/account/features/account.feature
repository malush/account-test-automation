Feature: Account
  As an existing user of the system
  I want to be able to manage my account

  Background:
    Given the user already exists in the system
    And the user is logged in

  Scenario: Create new account
    When the user tries to create a new account with valid data
    Then the new account is successfully created

  Scenario Outline: Missing fields
    When the user tries to create a new account with missing "<inputData>"
    Then the account creation fails with Bad Request response
    Examples:
      |inputData    |
      |nameOnAccount|
      |currencyId   |

  Scenario: Missing token
    When the user tries to create a new account without providing access token
    Then the access to account resource is forbidden

  Scenario: Invalid token
    When the user tries to create a new account but provides an invalid token
    Then the access to account resource is forbidden

  Scenario: Account exists
    Given the account already exists in the system
    When the user tries to create a new account with valid data
    Then the account creation fails with the response indicating the conflict

    #account currency not supported / or validation error