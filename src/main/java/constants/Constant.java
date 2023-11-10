package constants;


public class Constant {

    public static final String BASE_URL = "https://restful-booker.herokuapp.com/booking";

    /* File constants*/
    public static final String BASE_FILE_PATH = "src\\test\\resources\\";
    public static final String BASE_API_REQUIEST_BODY = BASE_FILE_PATH + "createBookingPostRequest.txt";
    public static final String BOOKING_JSON_SCHEMA = BASE_FILE_PATH + "bookingJsonValidationSchema.txt";

    /* Json path constants */

    public static final String FIRST_NAME_JPATH = "booking.firstname";
    public static final String LAST_NAME_JPATH = "booking.lastname";
    public static final String CHECKIN_DATE_JPATH = "booking.bookingdates.checkin";
}
