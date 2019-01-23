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

  Scenario: Unknown account number
    Given the user chooses the wrong account number
    When the user tries to get the account
    Then the account cannot be found

  Scenario: Required account response fields
    Given the account with account details: 'test' and 'EUR' already exists
    When the user tries to get the account
    Then the users account is successfully retrieved
    And the account response contains all required fields

  Scenario Outline: Opening balance for new accounts
    Given the user inserts account details: 'Ivan Malusev' and 'EUR'
    And the request balance value is '<reqBalance>'
    When the user tries to create a new account
    Then the new account is successfully created
    And the account balance is '<resBalance>'
    Examples:
      |reqBalance|resBalance|
      |missing   |0.00      |
      |100       |100.00    |

  Scenario Outline: Optional account response fields
    Given the user inserts account details: 'Ivan Malusev' and 'EUR'
    And the request account type value is '<accountType>'
    And the request balance value is '<balance>'
    And the request balance status is '<balanceStatus>'
    When the user tries to create a new account
    Then the new account is successfully created
    And the created values for '<accountType>', '<balance>' and '<balanceStatus>' are correct
    Examples:
      |accountType|balance|balanceStatus|
      |CLIENT     |10     |DR           |