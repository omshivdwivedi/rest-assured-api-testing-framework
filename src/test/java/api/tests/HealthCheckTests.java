package api.tests;

import org.testng.annotations.Test;
import static org.testng.Assert.*;
import api.endpoints.PingAPI;
import io.restassured.response.Response;

public class HealthCheckTests extends BaseTest {
 @Test
 public void verifyHealthCheck() {

        Response response = PingAPI.healthCheck();

        response.then().log().all();

        assertEquals(response.getStatusCode(), 201);
        assertEquals(response.getBody().asString(), "Created");
    }
    
}
