package ai.springmtcoding.handler.ex;

public class CustomApiException extends RuntimeException {

    public CustomApiException(final String message) {
        super(message);
    }
}
