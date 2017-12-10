package top.itning.ta.exception;


/**
 * Json格式所查找的数据没有找到
 *
 * @author Ning
 */
public class JsonDataNotFindException extends JsonException {
    /**
     * 异常消息
     */
    private String exceptionMessage;

    public JsonDataNotFindException(String exceptionMessage) {
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