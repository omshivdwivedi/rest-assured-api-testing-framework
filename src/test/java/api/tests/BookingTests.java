package api.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import api.endpoints.AuthAPI;
import api.endpoints.BookingAPI;
import api.payloads.AuthReqPayload;
import api.payloads.AuthResPayload;
import api.payloads.BookingDates;
import api.payloads.BookingReqPayload;
import api.payloads.BookingResPayload;
import api.utils.PropertyReader;
import io.restassured.response.Response;

public class BookingTests extends BaseTest {

    private String token;
    private int bookingId;

    @BeforeClass
    public void generateToken() {

        String username = PropertyReader.getProperty("auth_username");
        String password = PropertyReader.getProperty("auth_password");


        AuthReqPayload auth = new AuthReqPayload();
        auth.setUsername(username);
        auth.setPassword(password);

        token = AuthAPI.createToken(auth)
                       .as(AuthResPayload.class)
                       .getToken();
    }


    private BookingReqPayload createPayload() {

        BookingDates dates = new BookingDates("2026-07-10", "2026-07-15");

        BookingReqPayload payload = new BookingReqPayload();

        payload.setFirstname("Aman");
        payload.setLastname("Kumar");
        payload.setTotalprice(500);
        payload.setDepositpaid(true);
        payload.setBookingdates(dates);
        payload.setAdditionalneeds("Breakfast");

        return payload;
    }


    @Test(priority = 1)
    public void createBooking() {

        Response response = BookingAPI.createBooking(createPayload());

        response.then().log().all();

        assertEquals(response.statusCode(), 200);

        BookingResPayload booking = response.as(BookingResPayload.class);

        bookingId = booking.getBookingid();

        assertTrue(bookingId > 0);
    }


    @Test(priority = 2, dependsOnMethods = "createBooking")
    public void getBookingById() {

        Response response = BookingAPI.getBookingById(bookingId);

        response.then().log().all();

        assertEquals(response.statusCode(), 200);
    }


    @Test(priority = 2, dependsOnMethods = "createBooking")
    public void validateGetBookingJsonSchema() {

        Response response = BookingAPI.getBookingById(bookingId);

        response.then().log().all();

        assertEquals(response.statusCode(), 200);

        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/getBookingSchema.json"));
    }

    
    @Test(priority = 3)
    public void getAllBookings() {

        Response response = BookingAPI.getAllBookingIds();

        assertEquals(response.statusCode(), 200);
    }


    @Test(priority = 4)
    public void getBookingsByName() {

        Response response = BookingAPI.getBookingIdsByName("Aman", "Kumar");

        assertEquals(response.statusCode(), 200);
    }


    @Test(priority = 5)
    public void getBookingsByDate() {

        Response response = BookingAPI.getBookingIdsByDate("2026-07-10","2026-07-15");

        assertEquals(response.statusCode(), 200);
    }


    @Test(priority = 6, dependsOnMethods = "createBooking")
    public void updateBooking() {

        BookingReqPayload payload = createPayload();
        payload.setFirstname("Aravind");

        Response response =BookingAPI.updateBooking(payload, bookingId, token);

        assertEquals(response.statusCode(), 200);
    }


    @Test(priority = 7, dependsOnMethods = "createBooking")
    public void partialUpdateBooking() {

        BookingReqPayload payload =
                new BookingReqPayload();

        payload.setFirstname("Mike");

        Response response =
                BookingAPI.partialUpdateBooking(payload, bookingId, token);

        assertEquals(response.statusCode(), 200);
    }


    @Test(priority = 8, dependsOnMethods = "createBooking")
    public void deleteBooking() {

        Response response = BookingAPI.deleteBooking(bookingId, token);
        assertEquals(response.statusCode(), 201);
    }


    @Test(priority = 9, dependsOnMethods = "deleteBooking")
    public void verifyBookingDeleted() {

        Response response = BookingAPI.getBookingById(bookingId);

        assertEquals(response.statusCode(), 404);
    }

}
