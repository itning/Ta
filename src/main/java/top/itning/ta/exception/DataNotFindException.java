package top.itning.ta.exception;

/**
 * 所查找的数据没有找到
 *
 * @author wangn
 */
public class DataNotFindException extends DefaultException {
    /**
     * 异常消息
     */
    private String exceptionMessage;

    public DataNotFindException(String exceptionMessage) {
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
