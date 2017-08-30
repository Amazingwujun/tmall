package com.tmall.entity.vo;


import com.alibaba.fastjson.annotation.JSONField;

/**
 * 服务器基本响应对象
 *
 */
public class ReturnBean {
    
    private static final int SUCCESS_CODE = 0;
    
    private String msg;

    private Object data;

    private int code;
    
    @JSONField(serialize = false)
    private boolean isSuccess;

    public ReturnBean(){}

    public ReturnBean(String msg, Object data, int code) {
        this.msg = msg;
        this.data = data;
        this.code = code;
    }

    public static ReturnBean success(String msg, Object data) {
        return new ReturnBean(msg, data, SUCCESS_CODE);
    }

    public static ReturnBean successWithMessage(String msg) {
        return new ReturnBean(msg, null, SUCCESS_CODE);
    }

    public static ReturnBean error(String msg, int code) {
        return new ReturnBean(msg, null, code);
    }
    
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ReturnBean{" +
                "msg='" + msg + '\'' +
                ", data=" + data +
                ", code=" + code +
                '}';
    }
}
