package ivancroce.event_platform.payloads;

import java.time.LocalDateTime;
import java.util.List;

public record ValidationErrorRespDTO(String message, LocalDateTime timestamp, List<String> errorsList) {
}
