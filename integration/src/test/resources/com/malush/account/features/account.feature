Feature: Account
  As an existing user of the system
  I want to be able to manage my account

  Background:
    Given the user already exists in the system
    And the user is logged in

  Scenario: Create new account
    When the user requests to create a new account with valid data
    Then the new account is successfully created
