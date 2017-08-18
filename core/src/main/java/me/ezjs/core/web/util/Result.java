package me.ezjs.core.web.util;

/**
 * Created by zero-mac on 16/6/28.
 */
public class Result {


    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    /**
     * 返回内容
     */
    private Object data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = "";
    }

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(ResultStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = null;
    }

    public Result(ResultStatus status, Object data) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = data;
    }

    public static Result ok(Object data) {
        return data(data);
    }

    public static Result data(Object data) {
        return new Result(ResultStatus.SUCCESS, data);
    }

    public static Result ok() {
        return new Result(ResultStatus.SUCCESS);
    }

    public static Result error(ResultStatus error) {
        return new Result(error);
    }

    public static Result error(String msg) {
        return new Result(ResultStatus.SERVER_ERROR.getCode(), msg);
    }

    ////error状态下不会返回data
    public static Result error(ResultStatus error, String msg) {
        if (msg == null) {
            msg = error.getMessage();
        }
        return new Result(error.getCode(), msg, null);
    }

//    public static Result error(ResultStatus error, Object data) {
//        return new Result(error, data);
//    }
}
