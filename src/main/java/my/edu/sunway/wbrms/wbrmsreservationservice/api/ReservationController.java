package my.edu.sunway.wbrms.wbrmsreservationservice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.sunway.wbrms.wbrmsreservationservice.entity.Reservation;
import my.edu.sunway.wbrms.wbrmsreservationservice.entity.Status;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/reservation")
@Tag(name = "Reservation API", description = "API for CRUD operations on reservations")
public class ReservationController {

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Create a new reservation")
    public Mono<Reservation> createReservation(@RequestBody Reservation reservation) {
        reservation = Reservation.withStatusAndCreatedTime(reservation, LocalDateTime.now(), Status.Created);
        return Mono.just(reservation);
    }

    @PostMapping("/cancel/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Reservation> cancelReservation(@PathVariable(name = "id") UUID id) {
        return Mono.empty();
    }

}
