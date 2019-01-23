Feature: Load account
  As an existing user of the system
  I want to be able to load an account

  Background:
    Given the user is logged in
    And the debit account exists with '1000.00' 'EUR'
    And the credit account exists with '100.00' 'EUR'

  Scenario Outline: Load account successfully
    Given the users load account amount input is '50' 'EUR'
    When the user tries to load the credit account
    Then the load account operation was successful
    And the '<account>' account balance is '<balance>'
    Examples:
      |account|balance|
      |credit |50     |
      |debit  |950    |