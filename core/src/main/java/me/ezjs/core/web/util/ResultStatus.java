package me.ezjs.core.web.util;

/**
 * 自定义请求状态码
 * Created by zero-mac on 16/6/17.
 */
public enum ResultStatus {

    SUCCESS(200, ""),
    SERVER_ERROR(500, "开发状态--严重异常,程序无法正常运行,请开发人员检查问题"),// 由开发人员代码不严谨造成的异常
    RUNTIME_SERVER_ERROR(502, "运行状态--异常通知,请开发人员检查问题并作出正确提示"),// 由开发人员代码不严谨造成的异常, 如未进行判空处理等,导致未能返回贴合业务的提示信息
    FRAME_SERVER_ERROR(504, "运行状态--异常通知,请框架维护人员检查问题并作出正确提示"),// 数据处理异常提醒, 包括传入数据不正确等, 异常由框架发起
    APPLICATION_SERVER_ERROR(506, "运行状态--异常通知,请开发人员检查问题并作出正确提示"),// 数据处理异常提醒, 包括传入数据不正确等, 异常由开发人员发起

    //FIXME:已经变成异常处理机制,该方式将不再支持
    USERNAME_OR_PASSWORD_ERROR(-1001, "用户名或密码错误"),
    USER_NOT_FOUND(-1002, "用户不存在"),
    USER_NOT_LOGIN(-1003, "用户未登录"),
    SEND_VERIFICATION_CODE_ERROR(-1004, "验证码发送失败"),
    VALIDATE_VERIFICATION_CODE_ERROR(-1005, "验证码校验失败"),
    PHONE_NOT_EXIST_ERROR(-1006, "未绑定手机号"),
    TOKEN_NOT_FOUND(-1007, ""),
    NO_PERMISSION(-1008, "用户无权限"),
    SCHOOL_NOT_EXIST(-1009, "学校信息不存在"),
    EXISTS_RESOURCE(-1011, "记录已存在"),
    PARAMETER_ERROR(-1013, "参数错误"),;
    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    ResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
