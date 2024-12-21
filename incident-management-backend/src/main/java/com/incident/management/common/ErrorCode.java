package com.incident.management.common;

/**
 * 错误码
 */
public enum ErrorCode {
    PARAMS_ERROR(1000,"请求参数错误",""),
    NULL_ERROR(1001,"请求数据为空",""),
    NOT_LOGIN(1002,"未登录",""),
    NO_AUTH(1003,"无权限",""),
    NO_FOUND_ERROR(1004,"请求数据不存在",""),
    SYSTEM_ERROR(1005,"系统内部异常",""),
    ;
    /**
     * 状态码
     */
    private final int code;
    /**
     * 状态码信息
     */
    private final String message;
    /**
     * 状态码描述（详情）
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
