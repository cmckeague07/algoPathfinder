package algoPathfinder.util;

import net.thucydides.model.util.EnvironmentVariables;
import net.thucydides.model.environment.SystemEnvironmentVariables;

public class EnvironmentConfig {

    public static String getBaseUrl() {
        EnvironmentVariables env = SystemEnvironmentVariables.createEnvironmentVariables();
        return env.getProperty("restassured.baseurl", "http://localhost:8080");
    }
}