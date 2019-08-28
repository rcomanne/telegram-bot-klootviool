package nl.rcomanne.telegrambotklootviool.exception;

public class MessageFailedException extends RuntimeException {

    public MessageFailedException(final String message) {
        super(message);
    }

    public MessageFailedException(final Throwable cause) {
        super(cause);
    }
}
