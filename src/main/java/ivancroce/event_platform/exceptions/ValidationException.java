package ivancroce.event_platform.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    private List<String> errorMessages;

    public ValidationException(List<String> errorMessages) {
        super("Various errors of validation.");
        this.errorMessages = errorMessages;
    }
}
