package my.edu.sunway.wbrms.wbrmsreservationservice.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import my.edu.sunway.wbrms.wbrmsreservationservice.DatabaseIntegrationTest;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.Reservation;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.SearchRequest;
import my.edu.sunway.wbrms.wbrmsreservationservice.dto.Status;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class ReservationControllerTest extends DatabaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Tag("negative")
    @DisplayName("Test case: Creation reservation with 0 pax is prohibited")
    void testThatControllerIsNotAcceptingReservationWithZeroPax() throws Exception {
        var reservation = new Reservation(null, LocalDateTime.now(),
                "Egor Voronianskii", LocalDateTime.now(),
                0, null, null);

        mockMvc.perform(post("/create")
                        .content(objectMapper.writeValueAsBytes(reservation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Tag("negative")
    @DisplayName("Test case: Creation reservation with blank name is prohibited")
    void testThatControllerIsNotAcceptingReservationWithBlankName() throws Exception {
        var reservation = new Reservation(
                null,
                LocalDateTime.now(),
                null,
                LocalDateTime.now(),
                10,
                null,
                null
        );

        mockMvc.perform(post("/create")
                        .content(objectMapper.writeValueAsBytes(reservation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Tag("negative")
    @DisplayName("Test case: Creation reservation with blank phone is prohibited")
    void testThatControllerIsNotAcceptingReservationWithEmptyPhone() throws Exception {
        var reservation = new Reservation(
                null,
                LocalDateTime.now(),
                "Ekaterina Voronianskaia",
                LocalDateTime.now(),
                10,
                null,
                null
        );

        mockMvc.perform(post("/create")
                        .content(objectMapper.writeValueAsBytes(reservation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Tag("negative")
    @DisplayName("Test case: Updating existing reservation with new information")
    void testThatUpdatingWorksCorrectWithNotExistingReservation() throws Exception {
        var reservation = new Reservation(
                null,
                LocalDateTime.now(),
                "Ekaterina Voronianskaia",
                LocalDateTime.now(),
                5,
                "013-387-xxxx",
                null
        );

        var createResult = mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(reservation)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        var savedReservation = objectMapper.readValue(createResult.getResponse().getContentAsString(), Reservation.class);
        Assertions.assertNotNull(savedReservation);

        var updateResult = mockMvc.perform(put("/update/" + UUID.randomUUID())
                        .content(objectMapper.writeValueAsString(reservation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        var problemDetail = objectMapper.readValue(updateResult.getResponse().getContentAsString(), ProblemDetail.class);
        Assertions.assertNotNull(problemDetail);
        Assertions.assertTrue(StringUtils.isNotBlank(problemDetail.getTitle()));
        Assertions.assertTrue(StringUtils.isNotBlank(problemDetail.getDetail()));

    }

    @Test
    @Tag("negative")
    @DisplayName("Test case: Delete non-existing reservation")
    void testThatServiceShowDedicatedErrorWhenIsReservationIsNotExisting() throws Exception {
        var problemDetailRaw = mockMvc.perform(delete("/cancel/" + UUID.randomUUID()))
                .andExpect(status().is4xxClientError())
                .andReturn();

        var problemDetail = objectMapper.readValue(problemDetailRaw.getResponse().getContentAsString(), ProblemDetail.class);


        Assertions.assertNotNull(problemDetail);
        Assertions.assertTrue(StringUtils.isNotBlank(problemDetail.getTitle()));
        Assertions.assertTrue(StringUtils.isNotBlank(problemDetail.getDetail()));
    }

    @Test
    @Tag("positive")
    @DisplayName("Test case: Creation reservation with valid information")
    void testThatControllerIsAcceptingValidReservation() throws Exception {
        var reservation = new Reservation(
                null,
                LocalDateTime.now(),
                "Ekaterina Voronianskaia",
                LocalDateTime.now(),
                5,
                "013-387-xxxx",
                null
        );

        mockMvc.perform(post("/create")
                        .content(objectMapper.writeValueAsBytes(reservation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andDo(print());
    }

    @Test
    @Tag("positive")
    @DisplayName("Test case: Updating existing reservation with new information")
    void testThatMethodOfUpdatingReservationWorksCorrect() throws Exception {
        var reservation = new Reservation(
                null,
                LocalDateTime.now(),
                "Ekaterina Voronianskaia",
                LocalDateTime.now(),
                5,
                "013-387-xxxx",
                null
        );

        var savedReservationRaw = mockMvc.perform(post("/create")
                        .content(objectMapper.writeValueAsBytes(reservation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andReturn();

        var savedReservation = objectMapper.readValue(savedReservationRaw.getResponse().getContentAsString(), Reservation.class);
        Assertions.assertNotNull(savedReservation);
        savedReservation = Reservation.withStatus(savedReservation, Status.Created);

        var updatedReservationRaw = mockMvc.perform(put("/update/" + savedReservation.id())
                        .content(objectMapper.writeValueAsBytes(savedReservation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        var updatedReservation = objectMapper.readValue(updatedReservationRaw.getResponse().getContentAsString(), Reservation.class);
        Assertions.assertNotNull(updatedReservation);
        Assertions.assertEquals(Status.Created, updatedReservation.status());
    }

    @Test
    @Tag("positive")
    @DisplayName("Test case: Deleting existing reservation")
    void testThatReservationStatusIsUpdateWhenServiceIsDeletingData() throws Exception {
        var reservation = new Reservation(
                null,
                LocalDateTime.now(),
                "Ekaterina Voronianskaia",
                LocalDateTime.now(),
                5,
                "013-387-xxxx",
                null
        );

        var savedReservationRaw = mockMvc.perform(post("/create")
                        .content(objectMapper.writeValueAsBytes(reservation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andReturn();

        var savedReservation = objectMapper.readValue(savedReservationRaw.getResponse().getContentAsString(), Reservation.class);
        Assertions.assertNotNull(savedReservation);
        savedReservation = Reservation.withStatus(savedReservation, Status.Created);

        mockMvc.perform(delete("/cancel/" + savedReservation.id()))
                .andExpect(status().isNoContent());
    }

    @Test
    @Tag("positive")
    @DisplayName("Test case: Searching reservation by parameters")
    void testThatReservationSearchingWorksCorrectly() throws Exception {
        var now = LocalDateTime.now();
        var reservation = new Reservation(
                null,
                now,
                "Ekaterina Voronianskaia",
                now,
                5,
                "013-387-xxxx",
                null
        );

        mockMvc.perform(post("/create")
                        .content(objectMapper.writeValueAsBytes(reservation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        var searchRequest = new SearchRequest(reservation.name(),
                reservation.phone(), reservation.pax(), null,
                now, null);

        var foundReservations = mockMvc.perform(post("/search")
                        .content(objectMapper.writeValueAsBytes(searchRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        List<Reservation> reservations = objectMapper.readValue(
                foundReservations.getResponse().getContentAsString(),
                new TypeReference<>(){});
        Assertions.assertNotNull(reservations);
        Assertions.assertFalse(reservations.isEmpty());
        Assertions.assertEquals(1, reservations.size());

        for (Reservation foundReservation : reservations) {
            Assertions.assertNotNull(foundReservation.id());
        }
    }


}