Feature: Login
  As an existing user of the system
  I want to be able to login and have access to the system

  Scenario Outline: Successful login
    Given the user with '<name>' and '<password>' already exists in the system
    And the user inserts login credentials: '<name>' and '<password>'
    When the user requests to login
    Then the access is granted
    Examples:
      |name   |password  |
      |malush |qwerty123 |

  Scenario: Login after sign up
    When a user successfully signs up
    Then the user is immediately granted access to the system

  Scenario: User doesn't exist
    Given an unknown user inserts login credentials
    When the user requests to login
    Then the access is forbidden

  Scenario Outline: Wrong password
    Given the user with '<name>' and '<password>' already exists in the system
    And the user inserts login credentials: '<name>' and '<wrongPassword>'
    When the user requests to login
    Then the access is forbidden
    Examples:
      |name   |password  | wrongPassword |
      |malush |qwerty123 | qwerty        |

  Scenario Outline: Missing or empty fields
    Given the user inserts login credentials: '<name>' and '<password>'
    When the user requests to login
    Then the access is forbidden
    Examples:
      |name   |password  |
      |null   |qwerty123 |
      |       |qwerty123 |
      |malush |null      |
      |malush |          |
      |null   |null      |
      |       |          |