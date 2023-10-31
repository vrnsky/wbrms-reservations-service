package my.edu.sunway.wbrms.wbrmsreservationservice.exception;

public class NotFoundReservation extends RuntimeException {

    public NotFoundReservation(String message) {
        super(message);
    }
}
