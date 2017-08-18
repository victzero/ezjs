package me.ezjs.core.exception;

public class JSONException extends ZeroAppException {

	private static final long serialVersionUID = -5245024631152917375L;

	public JSONException() {}

	public JSONException(String e) {
		super(e);
	}

	public JSONException(Exception e) {
		super(e);
	}
}
