package ivancroce.event_platform.payloads;

import jakarta.validation.constraints.NotEmpty;

public record LoginDTO(
        @NotEmpty(message = "Username is mandatory")
        String username,
        @NotEmpty(message = "Password is mandatory")
        String password) {
}
