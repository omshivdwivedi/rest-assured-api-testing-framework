package api.endpoints;

import static io.restassured.RestAssured.given;

import api.payloads.BookingReqPayload;
import api.utils.ExtentReportManager;
import api.utils.PropertyReader;
import io.restassured.response.Response;

public class BookingAPI {

    private static String endpoint = PropertyReader.getProperty("baseurl")+"booking/";

    public static Response getAllBookingIds() {
        
        Response response = given()
                            .when()
                                .get(endpoint);

        ExtentReportManager.logApiDetails("/booking","GET",endpoint,response);

        return response;
    }


     public static Response getBookingIdsByName(String firstName, String lastName) {
        
        Response response = given()
                                .param("firstname", firstName)
                                .param("lastname", lastName)
                            .when()
                                .get(endpoint);

        String url = endpoint +"?firstname=" + firstName +"&lastname=" + lastName;
        ExtentReportManager.logApiDetails("/booking","GET",url,response);

        return response;
    }


     public static Response getBookingIdsByDate(String checkin, String checkout) {
        
        Response response = given()
                                .param("checkin", checkin)
                                .param("checkout", checkout)
                            .when()
                                .get(endpoint);

        String url = endpoint +"?checkin=" + checkin +"&checkout=" + checkout;
        ExtentReportManager.logApiDetails("/booking","GET",url,response);

        return response;
    }


    public static Response getBookingById(int id) {

        Response response = given()
                                .contentType("application/json")    
                            .when()
                                .get(endpoint+id);

        ExtentReportManager.logApiDetails("/booking","GET",endpoint+id,response);

        return response;
    }


    public static Response createBooking(BookingReqPayload bookingReqPayload) {
        
        Response response = given()
                                .contentType("application/json")
                                .accept("application/json")
                                .body(bookingReqPayload)
                            .when()
                                .post(endpoint);

        ExtentReportManager.logApiDetails("/booking","POST",bookingReqPayload,response);

        return response;
    }
 

    public static Response updateBooking(BookingReqPayload bookingReqPayload, int id, String authToken) {
        
        Response response = given()
                                .contentType("application/json")
                                .accept("application/json")
                                .header("Cookie", "token=" + authToken)
                                .body(bookingReqPayload)
                            .when()
                                .put(endpoint+id);

        ExtentReportManager.logApiDetails("/booking","PUT",bookingReqPayload,response);

        return response;
    }


    public static Response partialUpdateBooking(BookingReqPayload bookingReqPayload, int id, String authToken) {
        
        Response response = given()
                                .contentType("application/json")
                                .accept("application/json")
                                .header("Cookie", "token=" + authToken)
                                .body(bookingReqPayload)
                            .when()
                                .patch(endpoint+id);

        ExtentReportManager.logApiDetails("/booking","PATCH",bookingReqPayload,response);

        return response;
    }


    public static Response deleteBooking(int id, String authToken) {
        
        Response response = given()
                                .header("Cookie", "token=" + authToken)    
                            .when()
                                .delete(endpoint+id);

        ExtentReportManager.logApiDetails("/booking","DELETE",endpoint+id,response);

        return response;
    }

    
}
