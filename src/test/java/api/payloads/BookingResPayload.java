package api.payloads;

public class BookingResPayload {

    private int bookingid;
    private Booking booking;

    public BookingResPayload() {
    }

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

      @Override
    public String toString() {
        return "{" +
                "bookingid=" + bookingid +
                ", booking=" + booking +
                '}';
    }

    public static class Booking {

        private String firstname;
        private String lastname;
        private int totalprice;
        private boolean depositpaid;
        private BookingDates bookingdates;
        private String additionalneeds;

        public Booking() {
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

        public int getTotalprice() {
            return totalprice;
        }

        public void setTotalprice(int totalprice) {
            this.totalprice = totalprice;
        }

        public boolean isDepositpaid() {
            return depositpaid;
        }

        public void setDepositpaid(boolean depositpaid) {
            this.depositpaid = depositpaid;
        }

        public BookingDates getBookingdates() {
            return bookingdates;
        }

        public void setBookingdates(BookingDates bookingdates) {
            this.bookingdates = bookingdates;
        }

        public String getAdditionalneeds() {
            return additionalneeds;
        }

        public void setAdditionalneeds(String additionalneeds) {
            this.additionalneeds = additionalneeds;
        }

                @Override
        public String toString() {
            return "Booking{" +
                    "firstname='" + firstname + '\'' +
                    ", lastname='" + lastname + '\'' +
                    ", totalprice=" + totalprice +
                    ", depositpaid=" + depositpaid +
                    ", bookingdates=" + bookingdates +
                    ", additionalneeds='" + additionalneeds + '\'' +
                    '}';
        }
    }
    
}
