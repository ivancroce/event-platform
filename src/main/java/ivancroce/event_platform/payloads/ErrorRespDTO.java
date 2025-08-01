package ivancroce.event_platform.payloads;

import java.time.LocalDateTime;

public record ErrorRespDTO(String message, LocalDateTime timestamp) {
}
