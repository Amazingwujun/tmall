package com.tmall.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;

public class JSONObject {
    private String msg;

    private Object data;

    private int code;

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

    public static void main(String args[]) {
        /*ArrayList<Object> list = new ArrayList<>();
        ArrayList<Object> list1 = new ArrayList<>();
        ArrayList<Object> list2 = new ArrayList<>();

        list.add("list");
        list.add(list1);
        list1.add("list1");
        list1.add(list2);
        list2.add("list2");*/

        JSONObject object = new JSONObject();
        /*object.setData();*/
        /*object.setMsg("success");
        object.setData(new ArrayList<>());*/

        Object parse = JSON.toJSONString(object, SerializerFeature.WriteNullStringAsEmpty);
        System.out.println(parse.toString());
    }


}
