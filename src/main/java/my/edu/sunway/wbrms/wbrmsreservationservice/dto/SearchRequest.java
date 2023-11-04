package my.edu.sunway.wbrms.wbrmsreservationservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

@Schema(description = "Definition of search request")
public record SearchRequest(

        @Schema(description = "Searching by name", example = "Andrew")
        String name,

        @Schema(description = "Searching by phone", example = "0133877xx")
        String phone,

        @Schema(description = "Min number of persons")
        @Min(value = 1, message = "At least 1 person")
        Integer minPerson,

        @Schema(description = "Max number of persons")
        Integer maxPerson,

        @Schema(description = "Desired date time of reservation")
        LocalDateTime desiredDateTime,

        @Schema(description = "Status of reservation")
        Status status
) {
}
