Feature: Sign up
  As a new user of the system
  I want to be able to register a user profile with chosen credentials


  Scenario: Successful user sign up
    When a user tries to register a new profile with valid data
    Then the user sign up is successful


#  #todo: https://automationrhapsody.com/introduction-to-cucumber-and-bdd-with-examples/ data driven, parametrised example for username password validation errors
#  Scenario: User sign up fails for invalid data
#    When a user tries to register a new profile with invalid data
#    Then the user sign up fails with appropriate error message
#
#  Scenario: User sign up fails for existing users
#    Given the user already exists in the system
#    When a user tries to register a new profile with valid data
#    Then the user sign up fails with appropriate error message