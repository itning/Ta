package top.itning.ta.exception;

/**
 * Json格式空参数异常
 *
 * @author Ning
 */
public class JsonNullParameterException extends JsonException {
    /**
     * 异常消息
     */
    private String exceptionMessage;

    public JsonNullParameterException(String exceptionMessage) {
        super(exceptionMessage);
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}