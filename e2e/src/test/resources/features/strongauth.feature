Feature: Go Strong Auth

  Scenario: Baseline test
    Given Go CD is running
    Given Auth is disabled
    When I make a request to the go home page
    Then I see the go home page

  Scenario: Auth enabled
    Given Go CD is running
    Given Auth is enabled
    Given There are no existing users
    When I make a request to the go home page
    Then I am redirected to the login screen

  Scenario: Auth enabled with username / password using PBKDF2
    Given Go CD is running
    Given Auth is enabled
    Given A login exists for "aUser", "aSecret" using PBKDF2
    When I login
    And I make a request to the go home page
    Then I see the go home page

  Scenario: Auth enabled with username / password using bcrypt
    Given Go CD is running
    Given Auth is enabled
    Given A login exists for "aUser", "aSecret" using bcrypt
    When I login
    And I make a request to the go home page
    Then I see the go home page
