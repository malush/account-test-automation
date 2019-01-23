Feature: Load account
  As an existing user of the system
  I want to be able to load an account

  Background:
    Given the user is logged in
    And the 'ledger' account exists with balance of '1000.00' 'EUR'
    And the 'client' account exists with balance of '100.00' 'EUR'

  Scenario Outline: Load account successfully
    Given the user input for the amount to load is '50' 'EUR'
    When the user tries to load the client account
    Then the load account operation is successful
    And the '<accountType>' account balance is '<balance>'
    Examples:
      |accountType|balance|
      |client     |50     |
      |ledger     |1050    |