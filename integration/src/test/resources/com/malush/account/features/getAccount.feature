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