package automation.apitesting.tests;

import automation.apitesting.common.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import constants.Contant;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import models.Booking;
import models.BookingDates;
import net.minidev.json.JSONArray;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;


public class BookingApiRequest extends BaseTest {

    /* Get methods */
    @Test
    public void getAllBooking() {
        Response response = RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .baseUri(Contant.BASE_URL)
                .when()
                    .get()
                .then()
                    .assertThat()
//                    .log().body()  // print the response body
                    .log().ifValidationFails() // print all failed response
                    .statusCode(200)
                    .statusLine("HTTP/1.1 200 OK")
                    .header("Content-Type", "application/json; charset=utf-8")
                .extract()
                .response();

        Assert.assertTrue(response.getBody().asString().contains("bookingid"));
    }

    /* Post methods*/
    @Test
    public void createBooking () throws JsonProcessingException {

        /* Prepare a request */
//        JSONObject booking = new JSONObject();
//        JSONObject bookingDates = new JSONObject();
//
//        booking.put("firstname", "api testing");
//        booking.put("lastname", "tutorial");
//        booking.put("totalprice", 1000);
//        booking.put("depositpaid", true);
//        booking.put("additionalneeds", "breakfast");
//        booking.put("bookingdates", bookingDates);
//
//        bookingDates.put("checkin", "2023-11-01");
//        bookingDates.put("checkout", "2023-11-05");

        BookingDates bookingDates = new BookingDates("2023-11-01", "2023-11-05");
        Booking booking = new Booking("api testing", "tutorial", "breakfast", 1000, true, bookingDates);

        ObjectMapper objectMapper = new ObjectMapper();
        String requistBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);

        /* Create a new booking */
        Response response = RestAssured
                .given()
                    .contentType(ContentType.JSON)
//                    .body(booking.toString())
                    .body(requistBody)
                    .baseUri(Contant.BASE_URL)
                .when()
                    .post()
                .then()
                    .assertThat()
                    .statusCode(200)
                    .body("booking.firstname", Matchers.equalTo("api testing"))
                    .body("booking.bookingdates.checkin", Matchers.equalTo("2023-11-01"))
                .extract().response();

        /* Check that new booking is added */
        int bookingId = response.path("bookingid");
        System.out.println("##### boolingid " + bookingId);

        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .pathParam("bookingid" , bookingId)
                    .baseUri(Contant.BASE_URL)
                .when()
                  .get("/{bookingid}", bookingId)
                .then()
                    .assertThat()
                    .statusCode(200)
                    .body("firstname", Matchers.equalTo("api testing"))
                    .body("lastname", Matchers.equalTo("tutorial"));
    }

    @Test
    public void createBookingFromFile () {

        try {
            String requestBody = FileUtils.readFileToString(new File(Contant.BASE_API_REQUIEST_BODY), "UTF-8");
            String jsonSchema = FileUtils.readFileToString(new File(Contant.BOOKING_JSON_SCHEMA), "UTF-8");

            Response response = RestAssured
                    .given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .baseUri(Contant.BASE_URL)
                    .when()
                        .post()
                    .then()
                        .assertThat()
                        .statusCode(200)
                        .extract().response();

            Assert.assertEquals(getPropertyValueFromJsonArray(response, "$.booking..firstname"), "api testing");
            Assert.assertEquals(getPropertyValueFromJsonArray(response, "$.booking..lastname"), "tutorial");
            Assert.assertEquals(getPropertyValueFromJsonArray(response, "$.booking.bookingdates..checkin"), "2023-11-01");

            int bookingId = JsonPath.read(response.body().asString(), "$.bookingid");
            RestAssured
                    .given()
                        .contentType(ContentType.JSON)
                        .baseUri(Contant.BASE_URL)
                    .when()
                        .get("/{bookingid}", bookingId)
                    .then()
                        .assertThat()
                        .statusCode(200)
                        .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));



        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private String getPropertyValueFromJsonArray (Response response, String jsonPath) {
        JSONArray jsonArray = JsonPath.read(response.body().asString(), jsonPath);
        return (String) jsonArray.get(0);
    }

}
