@smoke
Feature: Smoke Test — AlgoPathFinder Framework Validation

  Scenario: Pathfinding service is available
    Given the AlgoPathFinder application is running
    Then the pathfinding service should be available

  Scenario: Results endpoint returns stored pathfinding results
    Given the AlgoPathFinder application is running
    When I send a GET request to "/results"
    Then the response status code is 200
    And the response body is a valid JSON array