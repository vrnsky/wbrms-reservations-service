package my.edu.sunway.wbrms.wbrmsreservationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.Reservation;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.SearchRequest;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.Status;
import my.edu.sunway.wbrms.wbrmsreservationservice.entity.ReservationEntity;
import my.edu.sunway.wbrms.wbrmsreservationservice.exception.NotFoundReservation;
import my.edu.sunway.wbrms.wbrmsreservationservice.repository.ReservationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static my.edu.sunway.wbrms.wbrmsreservationservice.entity.ReservationSpecification.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd.MM.yyyy");

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


    public List<Reservation> searchReservation(SearchRequest searchRequest) {
        Specification<ReservationEntity> specification = Specification.where(hasName(searchRequest.name()))
                .and(hasPhone(searchRequest.phone()))
                .and(hasPax(searchRequest.minPerson(), searchRequest.maxPerson()))
                .and(hasStatus(searchRequest.status()))
                .and(hasDesiredDateTime(searchRequest.desiredDateTime()));
        return Reservation.fromReservationEntityList(reservationRepository.findAll(specification));
    }


    public Map<String, List<Reservation>> list(int page, int size) {
        var pageRequest = PageRequest.of(page, size);
        var pagedReservations = reservationRepository.findByDesiredDateTimeBeforeOrderByDesiredDateTimeDesc(
                LocalDateTime.now().minusDays(7).toLocalDate().atStartOfDay(),
                pageRequest
        );

        Map<String, List<Reservation>> listOfReservationsPerDate = new HashMap<>();

        for (ReservationEntity reservation : pagedReservations.getContent()) {
            var reservationDate = DTF.format(reservation.getDesiredDateTime());
            listOfReservationsPerDate.computeIfAbsent(reservationDate, key -> new ArrayList<>())
                    .add(Reservation.fromReservationEntity(reservation));
        }

        for (List<Reservation> reservations : listOfReservationsPerDate.values()) {
            reservations.sort(new DateTimeComparator().reversed());
        }
        return listOfReservationsPerDate;
    }

    private static class DateTimeComparator implements Comparator<Reservation> {
        @Override
        public int compare(Reservation reservation1, Reservation reservation2) {
            return reservation2.desiredTime().compareTo(reservation1.desiredTime());
        }
    }
}
