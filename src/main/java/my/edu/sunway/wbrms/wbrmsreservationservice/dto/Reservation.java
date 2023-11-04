package my.edu.sunway.wbrms.wbrmsreservationservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import my.edu.sunway.wbrms.wbrmsreservationservice.entity.ReservationEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record Reservation(

        @Schema(description = "Unique identifier")
        UUID id,

        @Schema(description = "Time of creation")
        @DateTimeFormat(pattern = "dd/MM/yyyy HH:ss")
        LocalDateTime creationTime,
        @Schema(description = "Name of person under reservation")
        @NotBlank(message = "Message is required filed")
        String name,

        @Schema(description = "Desired date and time")
        @DateTimeFormat(pattern = "dd/MM/yyyy HH:ss")
        @NotNull(message = "Desired time must be not null")
        LocalDateTime desiredTime,

        @Schema(description = "Number of pax")
        @Min(value = 1, message = "At least one")
        int pax,

        @Schema(description = "Contact mobile phone")
        @NotBlank(message = "Contact phone must be not null")
        String phone,

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
                reservation.phone,
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
                reservation.phone,
                status
        );
    }

    public static Reservation fromReservationEntity(ReservationEntity reservationEntity) {
        return new Reservation(
                reservationEntity.getId(),
                reservationEntity.getCreationDateTime(),
                reservationEntity.getName(),
                reservationEntity.getDesiredDateTime(),
                reservationEntity.getPax(),
                reservationEntity.getPhone(),
                reservationEntity.getStatus()
        );
    }

    public static List<Reservation> fromReservationEntityList(List<ReservationEntity> reservationEntityList) {
        return reservationEntityList.stream()
                .map(Reservation::fromReservationEntity)
                .collect(Collectors.toList());
    }
}
