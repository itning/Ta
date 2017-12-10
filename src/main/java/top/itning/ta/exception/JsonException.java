package top.itning.ta.exception;

/**
 * Json异常
 *
 * @author Ning
 */
public class JsonException extends RuntimeException {
    public JsonException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
