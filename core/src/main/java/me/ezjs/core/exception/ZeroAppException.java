package me.ezjs.core.exception;

/**
 * 系统业务逻辑异常,会被BaseAction中的异常处理机制捕获,并且返回统一错误代码(code: 502)
 */
public class ZeroAppException extends RuntimeException {

	private static final long serialVersionUID = -8821832890385311686L;

	protected String msg;

	public ZeroAppException() {
	}

	public ZeroAppException(final String message) {
		super(message);
	}

	public ZeroAppException(Exception e) {
		super(e);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
