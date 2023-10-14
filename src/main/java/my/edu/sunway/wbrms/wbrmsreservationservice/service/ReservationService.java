package my.edu.sunway.wbrms.wbrmsreservationservice.service;

import my.edu.sunway.wbrms.wbrmsreservationservice.dto.ListOfReservations;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.SearchRequest;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.SortBy;
import my.edu.sunway.wbrms.wbrmsreservationservice.entity.Reservation;
import my.edu.sunway.wbrms.wbrmsreservationservice.entity.Status;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd-MM-yyyy");

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
