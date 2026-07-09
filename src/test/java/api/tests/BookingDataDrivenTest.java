package api.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.opencsv.exceptions.CsvValidationException;

import api.endpoints.BookingAPI;
import api.payloads.BookingDates;
import api.payloads.BookingReqPayload;
import api.payloads.BookingResPayload;
import api.utils.DataReaderCSV;
import io.restassured.response.Response;

public class BookingDataDrivenTest extends BaseTest {
    List<HashMap<String, String>> dataMap;

    @DataProvider(name = "bookingData")
    public Object[][] bookingData() throws IOException, CsvValidationException {

        dataMap = DataReaderCSV.data("src/test/resources/testdata/bookingData.csv");

        Object[][] data = new Object[dataMap.size()][1];

        for (int i = 0; i < dataMap.size(); i++) {
            data[i][0] = i + 1; 
        }

        return data;
    }

    @Test(dataProvider = "bookingData")
    public void createBooking(int row) {

        int index = row - 1;

        String firstname = dataMap.get(index).get("firstname");
        String lastname = dataMap.get(index).get("lastname");
        int totalprice = Integer.parseInt(dataMap.get(index).get("totalprice"));
        boolean depositpaid = Boolean.parseBoolean(dataMap.get(index).get("depositpaid"));
        String checkin = dataMap.get(index).get("checkin");
        String checkout = dataMap.get(index).get("checkout");
        String additionalneeds = dataMap.get(index).get("additionalneeds");

        BookingDates dates = new BookingDates(checkin, checkout);

        BookingReqPayload request = new BookingReqPayload();
        request.setFirstname(firstname);
        request.setLastname(lastname);
        request.setTotalprice(totalprice);
        request.setDepositpaid(depositpaid);
        request.setBookingdates(dates);
        request.setAdditionalneeds(additionalneeds);

        Response response = BookingAPI.createBooking(request);

        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);

        BookingResPayload booking = response.as(BookingResPayload.class);

        Assert.assertTrue(booking.getBookingid() > 0);
        Assert.assertEquals(booking.getBooking().getFirstname(), firstname);
        Assert.assertEquals(booking.getBooking().getLastname(), lastname);
    }
}
