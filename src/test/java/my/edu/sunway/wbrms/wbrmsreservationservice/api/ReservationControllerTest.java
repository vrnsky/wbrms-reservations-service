package my.edu.sunway.wbrms.wbrmsreservationservice.api;

import my.edu.sunway.wbrms.wbrmsreservationservice.DatabaseIntegrationTest;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.Reservation;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.Status;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDateTime;
import java.util.UUID;

class ReservationControllerTest extends DatabaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Tag("negative")
    @DisplayName("Test case: Creation reservation with 0 pax is prohibited")
    void testThatControllerIsNotAcceptingReservationWithZeroPax() {
        var reservation = new Reservation(
                null,
                LocalDateTime.now(),
                "Egor Voronianskii",
                LocalDateTime.now(),
                0,
                null,
                null
        );

        webTestClient.post()
                .uri("/create")
                .body(BodyInserters.fromValue(reservation))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Tag("negative")
    @DisplayName("Test case: Creation reservation with blank name is prohibited")
    void testThatControllerIsNotAcceptingReservationWithBlankName() {
        var reservation = new Reservation(
                null,
                LocalDateTime.now(),
                null,
                LocalDateTime.now(),
                10,
                null,
                null
        );

        webTestClient.post()
                .uri("/create")
                .body(BodyInserters.fromValue(reservation))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Tag("negative")
    @DisplayName("Test case: Creation reservation with blank phone is prohibited")
    void testThatControllerIsNotAcceptingReservationWithEmptyPhone() {
        var reservation = new Reservation(
                null,
                LocalDateTime.now(),
                "Ekaterina Voronianskaia",
                LocalDateTime.now(),
                10,
                null,
                null
        );

        webTestClient.post()
                .uri("/create")
                .body(BodyInserters.fromValue(reservation))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Tag("negative")
    @DisplayName("Test case: Updating existing reservation with new information")
    void testThatUpdatingWorksCorrectWithNotExistingReservation() {
        var reservation = new Reservation(
                null,
                LocalDateTime.now(),
                "Ekaterina Voronianskaia",
                LocalDateTime.now(),
                5,
                "013-387-xxxx",
                null
        );

        var savedReservation = webTestClient.post()
                .uri("/create")
                .body(BodyInserters.fromValue(reservation))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Reservation.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(savedReservation);

        var problemDetail = webTestClient.put()
                .uri("/update/" + UUID.randomUUID())
                .body(BodyInserters.fromValue(reservation))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(problemDetail);
        Assertions.assertTrue(StringUtils.isNotBlank(problemDetail.getTitle()));
        Assertions.assertTrue(StringUtils.isNotBlank(problemDetail.getDetail()));

    }

    @Test
    @Tag("negative")
    @DisplayName("Test case: Delete non-existing reservation")
    void testThatServiceShowDedicatedErrorWhenIsReservationIsNotExisting() {
        var problemDetail = webTestClient.delete()
                .uri("/cancel/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(problemDetail);
        Assertions.assertTrue(StringUtils.isNotBlank(problemDetail.getTitle()));
        Assertions.assertTrue(StringUtils.isNotBlank(problemDetail.getDetail()));
    }

    @Test
    @Tag("positive")
    @DisplayName("Test case: Creation reservation with valid information")
    void testThatControllerIsAcceptingValidReservation() {
        var reservation = new Reservation(
                null,
                LocalDateTime.now(),
                "Ekaterina Voronianskaia",
                LocalDateTime.now(),
                5,
                "013-387-xxxx",
                null
        );

        webTestClient.post()
                .uri("/create")
                .body(BodyInserters.fromValue(reservation))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @Tag("positive")
    @DisplayName("Test case: Updating existing reservation with new information")
    void testThatMethodOfUpdatingReservationWorksCorrect() {
        var reservation = new Reservation(
                null,
                LocalDateTime.now(),
                "Ekaterina Voronianskaia",
                LocalDateTime.now(),
                5,
                "013-387-xxxx",
                null
        );

        var savedReservation = webTestClient.post()
                .uri("/create")
                .body(BodyInserters.fromValue(reservation))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Reservation.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(savedReservation);
        savedReservation = Reservation.withStatus(savedReservation, Status.Created);

        var updatedReservation = webTestClient.put()
                .uri("/update/" + savedReservation.id())
                .body(BodyInserters.fromValue(reservation))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Reservation.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(updatedReservation);
        Assertions.assertEquals(Status.Created, updatedReservation.status());
    }

    @Test
    @Tag("positive")
    @DisplayName("Test case: Deleting existing reservation")
    void testThatReservationStatusIsUpdateWhenServiceIsDeletingData() {
        var reservation = new Reservation(
                null,
                LocalDateTime.now(),
                "Ekaterina Voronianskaia",
                LocalDateTime.now(),
                5,
                "013-387-xxxx",
                null
        );

        var savedReservation = webTestClient.post()
                .uri("/create")
                .body(BodyInserters.fromValue(reservation))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Reservation.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(savedReservation);
        savedReservation = Reservation.withStatus(savedReservation, Status.Created);

        webTestClient.delete()
                .uri("/cancel/" + savedReservation.id())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(204));
    }


}