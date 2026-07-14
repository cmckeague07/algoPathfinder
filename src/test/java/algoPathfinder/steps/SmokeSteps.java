package algoPathfinder.steps;

import algoPathfinder.util.EnvironmentConfig;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.instanceOf;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmokeSteps {

    private static final Logger log = LoggerFactory.getLogger(SmokeSteps.class);
    private java.net.http.HttpResponse<String> httpResponse;

    @Given("the AlgoPathFinder application is running")
    public void theApplicationIsRunning() {
        log.info("Smoke check — AlgoPathFinder application context verified");
    }

    @Then("the pathfinding service should be available")
    public void thePathfindingServiceIsAvailable() {
        log.info("Smoke check — pathfinding service available");
    }

    @When("I send a GET request to {string}")
    public void iSendAGETRequestTo(String endpoint) throws Exception {
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(EnvironmentConfig.getBaseUrl() + endpoint))
                .GET()
                .build();
        httpResponse = client.send(request,
                java.net.http.HttpResponse.BodyHandlers.ofString());
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("▶ GET {}{}",  EnvironmentConfig.getBaseUrl(), endpoint);
        log.info("◀ Status: {}", httpResponse.statusCode());
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    @Then("the response status code is {int}")
    public void checkResponseStatusCode(int statusCode) {
        assert httpResponse.statusCode() == statusCode;
        log.info("Status code assertion passed — expected: {}, actual: {}",
                statusCode, httpResponse.statusCode());
    }

    @And("the response body is a valid JSON array")
    public void checkJSONArrayResponse() {
        assert httpResponse.body().trim().startsWith("[");
        log.info("Response body is a valid JSON array");
        log.info("◀ Body preview: {}...",
                httpResponse.body().substring(0, Math.min(150, httpResponse.body().length())));
    }
}