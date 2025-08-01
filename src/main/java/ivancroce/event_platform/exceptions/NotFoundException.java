package ivancroce.event_platform.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super("Resource with id '" + id + "' not found.");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
