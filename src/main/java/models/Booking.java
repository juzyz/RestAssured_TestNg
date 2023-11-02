package models;

public class Booking {

    private String firstname;
    private String lastname;
    private String additionalneeds;
    private int totalprice;
    private Boolean depositpaid;
    private BookingDates bookingdates;

    public Booking() {
    }

    public Booking(String firstName, String lastName, String additionalNeeds, int totalPrice, boolean depositPaid, BookingDates bookingDates) {
        setFirstname(firstName);
        setLastname(lastName);
        setAdditionalneeds(additionalNeeds);
        setTotalprice(totalPrice);
        setDepositpaid(depositPaid);
        setBookingdates(bookingDates);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAdditionalneeds() {
        return additionalneeds;
    }

    public void setAdditionalneeds(String additionalneeds) {
        this.additionalneeds = additionalneeds;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public Boolean getDepositpaid() {
        return depositpaid;
    }

    public void setDepositpaid(Boolean depositpaid) {
        this.depositpaid = depositpaid;
    }

    public BookingDates getBookingdates() {
        return bookingdates;
    }

    public void setBookingdates(BookingDates bookingdates) {
        this.bookingdates = bookingdates;
    }


}