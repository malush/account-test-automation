Feature: Sign up
  As a new user of the system
  I want to be able to register a user profile with chosen credentials


  Scenario Outline: Successful sign up
    Given the user inserts '<name>' and '<password>'
    When the user tries to register a new profile
    Then the user sign up is successful
    Examples:
      |name   |password  |
      |malush |qwerty123 |

  Scenario Outline: Missing or empty fields
    Given the user inserts '<name>' and '<password>'
    When the user tries to register a new profile
    Then the user sign up fails with Bad Request response
    Examples:
      | name   | password  |
      | null   | qwerty123 |
      |        | qwerty123 |
      | malush | null      |
      | malush |           |

  Scenario Outline: User sign up fails for existing users
    Given the user with '<name>' and '<password>' already exists in the system
    When the user tries to register a new profile
    Then the user sign up fails with response indicating the conflict
    Examples:
      |name   |password  |
      |malush |qwerty123 |