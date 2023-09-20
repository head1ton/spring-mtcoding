package ai.springmtcoding.handler.ex;

public class CustomForbiddenException extends RuntimeException {

    public CustomForbiddenException(final String message) {
        super(message);
    }
}
