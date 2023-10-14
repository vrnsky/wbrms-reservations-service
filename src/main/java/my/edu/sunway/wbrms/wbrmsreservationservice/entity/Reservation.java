package my.edu.sunway.wbrms.wbrmsreservationservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record Reservation(

        @Schema(description = "Unique identifier")
        UUID id,

        @Schema(description = "Time of creation")
        @DateTimeFormat(pattern = "dd/MM/yyyy HH:ss")
        LocalDateTime creationTime,
        @Schema(description = "Name of person under reservation")
        String name,

        @Schema(description = "Desired date and time")
        @DateTimeFormat(pattern = "dd/MM/yyyy HH:ss")
        LocalDateTime desiredTime,

        @Schema(description = "Number of pax")
        int pax,

        @Schema(description = "Contact mobile phone")
        String mobilePhone,

        @Schema(description = "Status of reservation")
        Status status
) {

    public static Reservation withStatus(Reservation reservation, Status status) {
        return new Reservation(
                reservation.id,
                reservation.creationTime,
                reservation.name,
                reservation.desiredTime,
                reservation.pax,
                reservation.mobilePhone,
                status
        );
    }

    public static Reservation withStatusAndCreatedTime(Reservation reservation, LocalDateTime creationTime, Status status) {
        return new Reservation(
                reservation.id,
                creationTime,
                reservation.name,
                reservation.desiredTime,
                reservation.pax,
                reservation.mobilePhone,
                status
        );
    }
}
