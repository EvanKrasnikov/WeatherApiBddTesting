Feature: Get current weather
  As a user
  In order to obtain current weather of a given city
  I want to send HTTP request to http://api.weatherapi.com/v1

  Background:
    Given user prepares request specification

  @positive
  @critical
  @allure.id:A-1
  Scenario Template: Successful scenario
    Given query parameters "<q>" and "<key>"
    When user sends GET request
    Then response status code should be 200
    And response body should contain "<cityName>"
    And response body should contain current weather information
    Examples:
      | q        | key     | cityName  |
      | Brasilia | 2ee10f  | Brasilia  |
      | Cairo    | 960aa9  | Cairo     |
      | Moscow   | c6ab20  | Moscow    |
      | Paris    | 9f2d78  | Paris     |
      | Tokyo    | f21f21  | Tokyo     |

  @negative
  @allure.id:A-2
  Scenario Template: Query parameter 'key' is not provided
    Given query parameter q is "<q>"
    When user sends GET request
    Then response status code should be 401
    And response body should contain error code 1002
    And response body should contain error message
    """
    API key is invalid or not provided.
    """
    Examples:
      | q      |
      | Samara |

  @negative
  @allure.id:A-3
  Scenario Template: Query parameter 'q' is not provided
    Given query parameter key is "<key>"
    When user sends GET request
    Then response status code should be 400
    And response body should contain error code 1003
    And response body should contain error message
    """
    Parameter q is missing.
    """
    Examples:
      | key     |
      | b41b7f  |

  @negative
  @allure.id:A-4
  Scenario Template: No location found matching parameter 'q'
    Given query parameters "<q>" and "<key>"
    When user sends GET request
    Then response status code should be 400
    And response body should contain error code 1006
    And response body should contain error message
    """
    No matching location found.
    """
    Examples:
      | q         | key     |
      | Narnia123 | 77a701  |

  @negative
  @allure.id:A-5
  Scenario Template: Json body passed in bulk request is invalid
    Given invalid json body
      """
      {
        "locations": [
            {
                "q":
            }
        ]
      }
      """
    And query parameters "<q>" and "<key>"
    When user sends POST request
    Then response status code should be 400
    And response body should contain error code 9000
    And response body should contain error message
      """
      Json body passed in bulk request is invalid. Please make sure it is valid json with utf-8 encoding.
      """
    Examples:
      | q    | key    |
      | bulk | 03df24 |
