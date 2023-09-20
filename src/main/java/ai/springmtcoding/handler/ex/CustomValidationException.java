package ai.springmtcoding.handler.ex;

import java.util.Map;
import lombok.Getter;

@Getter
public class CustomValidationException extends Throwable {

    private final Map<String, String> errorMap;

    public CustomValidationException(final String message, final Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }
}
