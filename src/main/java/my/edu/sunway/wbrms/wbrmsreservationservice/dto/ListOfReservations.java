package my.edu.sunway.wbrms.wbrmsreservationservice.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "Entity describing reservations")
public record ListOfReservations(
        @Schema(description = "List of reservations per date")
        Map<String, List<Reservation>> reservations
) {
}
