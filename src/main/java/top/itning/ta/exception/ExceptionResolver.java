package top.itning.ta.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.itning.ta.entity.ServerMessage;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理
 *
 * @author wangn
 */
@ControllerAdvice
public class ExceptionResolver {
    /**
     * json 格式错误消息
     *
     * @param req HttpServletRequest
     * @param e   Exception
     * @return 异常消息
     * @throws Exception 异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ServerMessage jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ServerMessage serverMessage = new ServerMessage();
        if (e instanceof NullParameterException || e instanceof DataNotFindException) {
            serverMessage.setCode(ServerMessage.SERVICE_ERROR);
        } else {
            serverMessage.setCode(ServerMessage.NOT_FIND);
        }
        serverMessage.setMsg(e.getMessage());
        serverMessage.setUrl(req.getRequestURL().toString());
        return serverMessage;
    }
}
