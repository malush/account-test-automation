Feature: Transfer between accounts
  As an existing user of the system
  I want to be able to perform transfers between accounts

  Background:
    Given the user is logged in
    And the 'ledger' account exists with balance of '1000.00' 'EUR' and balance status 'DR'
    And the 'client' account exists with balance of '100.00' 'EUR' and balance status 'DR'
    And the 'client' account exists with balance of '30.00' 'EUR' and balance status 'CR'

    Scenario: Successful transfer between accounts
      Given the user selects the transfer amount: '5.00'
      And the user selects the transfer currency: 'EUR'
      And the user selects the transfer 'debit' account
      And the user selects the transfer 'credit' account
      When the user tries to transfer between accounts
      Then the transfer completed successfully
      And the transfer 'debit' account balance is '105'
      And the transfer 'credit' account balance is '35'