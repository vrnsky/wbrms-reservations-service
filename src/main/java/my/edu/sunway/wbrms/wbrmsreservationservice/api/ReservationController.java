package my.edu.sunway.wbrms.wbrmsreservationservice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.Reservation;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.SearchRequest;
import my.edu.sunway.wbrms.wbrmsreservationservice.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Reservation API", description = "API for CRUD operations on reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Create a new reservation")
    public Reservation createReservation(
            @Parameter(description = "Definition of reservation") @Valid @RequestBody Reservation reservation) {
        return reservationService.create(reservation);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update existing reservation")
    public Reservation updateReservation(
            @Parameter(description = "ID of reservation") @PathVariable(name = "id") UUID id,
            @Parameter(description = "Update reservation") @Valid @RequestBody Reservation reservation
    ) {
        return reservationService.update(id, reservation);
    }

    @DeleteMapping("/cancel/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelReservation(@PathVariable(name = "id") UUID id) {
        reservationService.cancelReservation(id);
    }

    @PostMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Reservation> searchReservations(@Valid @RequestBody SearchRequest searchRequest) {
        return reservationService.searchReservation(searchRequest);
    }


}
