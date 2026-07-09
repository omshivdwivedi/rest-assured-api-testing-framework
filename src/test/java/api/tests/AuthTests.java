package api.tests;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import api.endpoints.AuthAPI;
import api.payloads.AuthReqPayload;
import api.payloads.AuthResPayload;
import api.utils.PropertyReader;
import io.restassured.response.Response;

public class AuthTests extends BaseTest {

     @Test
    public void verifyTokenGeneration() {

       
        String username = PropertyReader.getProperty("auth_username");
        String password = PropertyReader.getProperty("auth_password");

        AuthReqPayload request = new AuthReqPayload();
        
        request.setUsername(username);
        request.setPassword(password);

        Response response = AuthAPI.createToken(request);

        response.then().log().all();

        assertEquals(response.getStatusCode(), 200);

        AuthResPayload authResponse = response.as(AuthResPayload.class);

        assertNotNull(authResponse.getToken());
        assertFalse(authResponse.getToken().isEmpty());
    }
    
}
