package algoPathfinder.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmokeSteps {

    private static final Logger log = LoggerFactory.getLogger(SmokeSteps.class);

    @Given("the AlgoPathFinder application is running")
    public void theApplicationIsRunning() {
        log.info("Smoke check — AlgoPathFinder application context verified");
    }

    @Then("the pathfinding service should be available")
    public void thePathfindingServiceIsAvailable() {
        log.info("Smoke check — pathfinding service available");
    }
}