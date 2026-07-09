package api.endpoints;

import static io.restassured.RestAssured.given;

import api.payloads.AuthReqPayload;
import io.restassured.response.Response;
import api.utils.ExtentReportManager;
import api.utils.PropertyReader;


public final class AuthAPI{

    private static String endpoint = PropertyReader.getProperty("baseurl")+"auth";

    public static Response createToken(AuthReqPayload authReqPayload){

        Response response = given()
                                .contentType("application/json")
                                .body(authReqPayload)
                            .when()
                                .post(endpoint);

        
    ExtentReportManager.logApiDetails("/auth","POST",authReqPayload,response);

        return response;

                        
    }
}