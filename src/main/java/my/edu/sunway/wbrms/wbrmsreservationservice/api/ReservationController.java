package my.edu.sunway.wbrms.wbrmsreservationservice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.Reservation;
import my.edu.sunway.wbrms.wbrmsreservationservice.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Reservation API", description = "API for CRUD operations on reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Create a new reservation")
    public Mono<Reservation> createReservation(
            @Parameter(description = "Definition of reservation") @Valid @RequestBody Reservation reservation) {
        return Mono.just(reservationService.create(reservation));
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update existing reservation")
    public Mono<?> updateReservation(
            @Parameter(description = "ID of reservation") @PathVariable(name = "id") UUID id,
            @Parameter(description = "Update reservation") @RequestBody Reservation reservation
    ) {
        return Mono.just(reservationService.update(id, reservation));
    }

    @DeleteMapping("/cancel/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<?> cancelReservation(@PathVariable(name = "id") UUID id) {
        reservationService.cancelReservation(id);
        return Mono.empty();
    }


}
