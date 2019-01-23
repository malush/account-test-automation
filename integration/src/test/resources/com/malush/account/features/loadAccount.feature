Feature: Load account
  As an existing user of the system
  I want to be able to load an account

  Background:
    Given the user is logged in
    And the 'ledger' account exists with balance of '1000.00' 'EUR'
    And the 'client' account exists with balance of '100.00' 'EUR'

  Scenario Outline: Load account successfully
    Given the user input for the amount to load is '<loadAmount>' 'EUR'
    When the user tries to load the client account
    Then the load account operation is successful
    And the '<accountType>' account balance is '<balance>'
    And the '<accountType>' account balance status is '<balanceStatus>'
    Examples:
      |loadAmount|accountType|balance|balanceStatus|
      |50        |client     |50     |CR           |
      |50        |ledger     |950    |DR           |
      |300       |client     |200    |DR           |
      |300       |ledger     |700    |DR           |
      |1000      |client     |900    |DR           |
      |1000      |ledger     |0      |DR           |
      |2000      |client     |1900   |DR           |
      |2000      |ledger     |1000   |CR           |


  Scenario Outline: Missing or empty fields
    Given the user input for the amount to load is '<loadAmount>' '<currencyId>'
    When the user tries to load the client account
    Then the account creation fails with Bad Request response
    Examples:
      |loadAmount   |currencyId |
      |null         |EUR        |
      |             |EUR        |
      |100.00       |null       |
      |100.00       |           |
      |null         |null       |
      |             |           |