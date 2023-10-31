package my.edu.sunway.wbrms.wbrmsreservationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.ListOfReservations;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.SearchRequest;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.SortBy;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.Reservation;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.Status;
import my.edu.sunway.wbrms.wbrmsreservationservice.entity.ReservationEntity;
import my.edu.sunway.wbrms.wbrmsreservationservice.exception.NotFoundReservation;
import my.edu.sunway.wbrms.wbrmsreservationservice.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final ReservationRepository reservationRepository;

    public Reservation create(Reservation reservation) {
        var reservationEntity = ReservationEntity.fromReservation(reservation);
        var savedReservation = reservationRepository.save(reservationEntity);
        return Reservation.fromReservationEntity(savedReservation);
    }

    public Reservation update(UUID originalId, Reservation reservation) {
        var existingReservation = reservationRepository.findById(originalId);
        if (existingReservation.isEmpty()) {
            throw new NotFoundReservation(String.format("No reservation found with ID = %s", originalId));
        }
        var updatedReservation = reservationRepository.save(ReservationEntity.fromReservation(reservation));
        return Reservation.fromReservationEntity(updatedReservation);
    }

    public void cancelReservation(UUID reservationId) {
        var existingReservation = reservationRepository.findById(reservationId);
        if (existingReservation.isEmpty()) {
            throw new NotFoundReservation(String.format("No reservation found with ID = %s", reservationId));
        }
        var reservation = existingReservation.get();
        reservation.setStatus(Status.Cancelled);
        reservationRepository.save(reservation);
    }

    public ListOfReservations getUpcomingReservations(SearchRequest searchRequest) {
        if (searchRequest.sortBy() == SortBy.CREATED_DATE) {
            var reservations = buildDefaultReservations()
                    .stream()
                    .collect(Collectors.groupingBy(reservation -> reservation.creationTime().toLocalDate().format(DTF)));
            return new ListOfReservations(reservations);
        }

        var reservations = buildDefaultReservations()
                .stream()
                .collect(Collectors.groupingBy(reservation -> reservation.desiredTime().toLocalDate().format(DTF)));

        return new ListOfReservations(reservations);
    }

    List<Reservation> buildDefaultReservations() {
        var reservations = new ArrayList<Reservation>();
        for (int index = 0; index < 20; index++) {
            var reservation = new Reservation(
                    UUID.randomUUID(),
                    LocalDateTime.now().plusHours(index).plusMinutes(index),
                    "Customer name",
                    LocalDateTime.now().plusDays(index).plusHours(index),
                    (index / 4 * 2) + 1,
                    "013-87" + index / 10 + "-176" + index / 10,
                    index % 2 == 0 ? Status.Pending : Status.Confirmed
            );
            reservations.add(reservation);
        }
        return reservations;
    }

}
