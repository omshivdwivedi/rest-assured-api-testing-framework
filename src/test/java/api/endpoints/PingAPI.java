package api.endpoints;

import static io.restassured.RestAssured.given;

import api.utils.ExtentReportManager;
import api.utils.PropertyReader;
import io.restassured.response.Response;

public class PingAPI {
    
    private static String endpoint = PropertyReader.getProperty("baseurl")+"ping";

    public static Response healthCheck(){

        Response response = given()
                            .when()
                                .get(endpoint);

        ExtentReportManager.logApiDetails("/ping","GET",endpoint,response);

        return response;

                        
    }
}
