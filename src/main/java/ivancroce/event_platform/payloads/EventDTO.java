package ivancroce.event_platform.payloads;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EventDTO(
        @NotEmpty(message = "Title is required")
        String title,

        @NotEmpty(message = "Description is required")
        String description,

        @NotNull(message = "Event date is required")
        @Future(message = "Event date must be in the future")
        LocalDate eventDate,

        @NotEmpty(message = "Location is required")
        String location,

        @NotNull(message = "Total seats are required")
        @Min(value = 1, message = "There must be at least 1 seat")
        Integer totalSeats
) {

}
