Feature: Top up account
  As an existing user of the system
  I want to be able to top up my account

  Background:
    Given the user is logged in
    And the users account exists

  Scenario: Load account successfully
    Given the user inserts the amount: '100' and currencyId: 'EUR'
    When the user tries to load an account
    Then the topUp of the users account was successful