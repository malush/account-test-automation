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

  Scenario: Invalid currency
    When the user tries to create a new account with invalid currency
    Then the account creation fails with Bad Request response


  Scenario: Multiple currencies
    Given the account already exists in the system
    When the user tries to add a new currency to existing account
    Then the account creation fails with the response indicating the conflict


  # It appears that the software currently has a bug since CurrencyValidator should allow only EUR but
  # validator accepts any non empty currency and uses Currency.getInstance to get an instance of it
  # In order to fix the issue we need to change the following line from the following class
  # Class: radu-solution/src/main/java/com/mine/payment/validator/CurrencyValidator.java
  # Line 22: should change from: if(!StringUtils.isBlank(value) || "EUR".equals(value))
  # to if(!StringUtils.isBlank(value) && "EUR".equals(value))
  # as the line 22 doesn't make any sense. It will always be true for any non empty value
  Scenario Outline: Supported currencies
    When the user tries to add a new account for one of the supported currencies: '<supportedCurrency>'
    Then the new account is successfully created
    Examples:
      |supportedCurrency|
      |EUR              |
      #|RSD              |
      #Currently all currencies are supported (for example, just uncomment the line above and this
      #scenario will still not fail although it should).
      #After the issue is fixed we should only have EUR in the list

