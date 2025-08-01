package ivancroce.event_platform.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserDTO(
        @NotEmpty(message = "The username is mandatory")
        @Size(min = 2, max = 30, message = "The username must be between 2 and 30 characters")
        String username,
        @NotEmpty(message = "The password is mandatory")
        @Size(min = 4, message = "The password must be at least 4 characters long") // just for this exercise min = 4
        String password,
        @NotEmpty(message = "The full name is mandatory")
        @Size(min = 2, max = 30, message = "The full name must be between 2 and 30 characters")
        String fullName
) {
}
