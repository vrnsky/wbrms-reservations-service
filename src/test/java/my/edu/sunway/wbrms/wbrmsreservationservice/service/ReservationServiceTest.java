package my.edu.sunway.wbrms.wbrmsreservationservice.service;

import my.edu.sunway.wbrms.wbrmsreservationservice.DatabaseIntegrationTest;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.Reservation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ReservationServiceTest extends DatabaseIntegrationTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    @DisplayName("Test case: Creation of a new reservation")
    void whenReservationIsCreatedCheckThatIdAssignedAndDateSaved() {
        Reservation reservation = new Reservation(null, null,
                "Egor Voronianskii", null, 0,
                "013-307-xxxx", null);
        var savedReservation = reservationService.create(reservation);
        Assertions.assertEquals(0, savedReservation.pax());
        Assertions.assertEquals("Egor Voronianskii", savedReservation.name());
        Assertions.assertNotNull(savedReservation.id());
    }

}