package me.ezjs.core.exception;

/**
 * 系统业务逻辑异常,会被BaseAction中的异常处理机制捕获,并且返回统一错误代码(code: 503)
 */
public class AppException extends ZeroAppException {

    private static final long serialVersionUID = -8821832890385311689L;

    protected String msg;

    public AppException() {
    }

    public AppException(final String message) {
        super(message);
    }

    public AppException(Exception e) {
        super(e);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
