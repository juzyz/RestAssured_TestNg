package automation.apitesting.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import java.io.File;
import java.io.IOException;
import com.jayway.jsonpath.JsonPath;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import automation.apitesting.common.BaseTest;
import models.Booking;
import models.BookingDates;
import constants.Constant;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;
import io.qameta.allure.Epic;


import static constants.Constant.FIRST_NAME_JPATH;

public class BookingApiRequestTests extends BaseTest {

    /* used for getBookingById test*/
    private int bookingId = 0;

    /* Get methods */
    @Epic("Regression Tests")
    @Test (description = "Get all bookings from db")
    public void getAllBookings() {
        Response response = RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .baseUri(Constant.BASE_URL)
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

    @Epic("Regression Tests")
    @Test(priority = 2, description = "Get booking by id of the created booking")
    public void getBookingByIdAndValidateOverJsonSchema() {
        try {
            String jsonSchema = FileUtils.readFileToString(new File(Constant.BOOKING_JSON_SCHEMA), "UTF-8");
            System.out.println("##### boolingid " + bookingId);

            RestAssured
                    .given()
                       .contentType(ContentType.JSON)
                        .baseUri(Constant.BASE_URL)
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

    /* Post methods*/
    @Epic("Regression Tests")
    @Test (description = "Create booking from the booking model")
    public void createBooking() throws JsonProcessingException {

        /* Prepare a request */
        BookingDates bookingDates = new BookingDates("2023-11-01", "2023-11-05");
        Booking booking = new Booking("api testing", "tutorial", "breakfast", 1000, true, bookingDates);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);

        /* Create a new booking */
        Response response = RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .baseUri(Constant.BASE_URL)
                .when()
                  .post()
                .then()
                    .assertThat()
                    .statusCode(200)
                    .body(FIRST_NAME_JPATH, Matchers.equalTo("api testing"))
                    .body(Constant.CHECKIN_DATE_JPATH, Matchers.equalTo("2023-11-01"))
                .extract().response();
    }

    @Epic("Regression Tests")
    @Test (description = "Create booking form json saved in txt file")
    public void createBookingFromFile() {
        try {
            String requestBody = FileUtils.readFileToString(new File(Constant.BASE_API_REQUIEST_BODY), "UTF-8");

            Response response = RestAssured
                    .given()
                      .contentType(ContentType.JSON)
                      .body(requestBody)
                      .baseUri(Constant.BASE_URL)
                    .when()
                        .post()
                    .then()
                        .assertThat()
                        .statusCode(200)
                        .extract().response();

            Assert.assertEquals(getPropertyValueFromJsonArray(response, "$." + FIRST_NAME_JPATH), "api testing");
            Assert.assertEquals(getPropertyValueFromJsonArray(response, "$." + Constant.LAST_NAME_JPATH), "tutorial");
            Assert.assertEquals(getPropertyValueFromJsonArray(response, "$." + Constant.CHECKIN_DATE_JPATH), "2023-11-01");

            bookingId = JsonPath.read(response.body().asString(), "$.bookingid");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getPropertyValueFromJsonArray(Response response, String jsonPath) {
        return JsonPath.read(response.body().asString(), jsonPath);
    }

}
