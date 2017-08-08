package com.tmall.common;


import com.alibaba.fastjson.annotation.JSONField;

/**
 * 服务器基本响应对象
 *
 */
public class JSONObject {
    
    private static final int SUCCESS_CODE = 0;
    
    private String msg;

    private Object data;

    private int code;
    
    @JSONField(serialize = false)
    private boolean isSuccess;

    public JSONObject(){}

    public JSONObject(String msg, Object data, int code) {
        this.msg = msg;
        this.data = data;
        this.code = code;
    }

    public static JSONObject success(String msg, Object data) {
        return new JSONObject(msg, data, SUCCESS_CODE);
    }

    public static JSONObject successWithMessage(String msg) {
        return new JSONObject(msg, null, SUCCESS_CODE);
    }

    public static JSONObject error(String msg,int code) {
        return new JSONObject(msg, null, code);
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
        return "JSONObject{" +
                "msg='" + msg + '\'' +
                ", data=" + data +
                ", code=" + code +
                '}';
    }
}
