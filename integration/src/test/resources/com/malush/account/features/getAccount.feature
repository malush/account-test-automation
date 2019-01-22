Feature: Get account
  As an existing user of the system
  I want to be able to get my account

  Background:
    Given the user is logged in


  Scenario Outline: Get account successfully
    Given the account with account details: '<nameOnAccount>' and '<currencyId>' already exists
    When the user tries to get the account
    Then the users account is successfully retrieved
    And the account has the following details: '<nameOnAccount>' and '<currencyId>'
    Examples:
      |nameOnAccount|currencyId |
      |Ivan Malusev |EUR        |

  Scenario: Check account response fields
    Given the account with account details: 'test' and 'EUR' already exists
    When the user tries to get the account
    Then the users account is successfully retrieved
    And the account response contains all required fields

  Scenario Outline: Initial balance for new accounts
    Given the user inserts account details: 'Ivan Malusev' and 'EUR'
    And the request account balance value is '<balance>'
    When the user tries to create a new account
    Then the new account is successfully created
    And the account balance is '<balance>'
    Examples:
      |balance  |
      |missing  |
      |100      |
