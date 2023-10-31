package my.edu.sunway.wbrms.wbrmsreservationservice.api;

import lombok.extern.slf4j.Slf4j;
import my.edu.sunway.wbrms.wbrmsreservationservice.exception.NotFoundReservation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ReservationControllerAdvice {

    @ExceptionHandler(NotFoundReservation.class)
    ProblemDetail handleNotFoundReservation(NotFoundReservation notFoundReservation) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, notFoundReservation.getMessage()
        );
        problemDetail.setTitle("Reservation with provided id is not existing");
        return problemDetail;
    }
}
