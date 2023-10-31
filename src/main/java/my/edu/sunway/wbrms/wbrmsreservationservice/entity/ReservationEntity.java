package my.edu.sunway.wbrms.wbrmsreservationservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.Reservation;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.Status;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity
@Table(name = "RESERVATIONS")
public class ReservationEntity {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private UUID id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PAX")
    private int pax;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "CREATION_DATE_TIME")
    private LocalDateTime creationDateTime;

    @Column(name = "DESIRED_DATE_TIME")
    private LocalDateTime desiredDateTime;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;


    public static ReservationEntity fromReservation(Reservation reservation) {
        var reservationEntity = new ReservationEntity();
        reservationEntity.setPax(reservation.pax());
        reservationEntity.setName(reservation.name());
        reservationEntity.setPhone(reservation.phone());
        reservationEntity.setCreationDateTime(reservation.creationTime());
        reservationEntity.setDesiredDateTime(reservation.desiredTime());
        reservationEntity.setStatus(Status.Created);
        return reservationEntity;
    }
}

